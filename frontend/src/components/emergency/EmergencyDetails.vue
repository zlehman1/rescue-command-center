<script setup>
import { onMounted, ref, onBeforeUnmount, nextTick, watch } from 'vue';
import Header from "../menu/Header.vue";
import { useI18n } from "vue-i18n";
import { VIcon, VList, VListItem, VBtn, VContainer, VCard, VCardText, VRow, VCol, VTextarea, VDialog, VCardActions, VTextField, VAutocomplete } from "vuetify/components";
import Footer from "../../components/menu/Footer.vue";
import { useTokenData } from "../../composables/useTokenData.js";
import { stateEnum } from "../../composables/stateEnum.js";
import MapComponent from "../Map/MapComponent.vue";
import { useFetch } from "@vueuse/core";
import Papa from "papaparse";

const { t } = useI18n();
const emergencyData = ref(null);
const messages = ref([]);
let socket;
const newMessage = ref('');
const path = ref('');
const username = ref('');
const isDispatcher = ref(false);
const bottomOfMessages = ref(null);
const color = ref('');
const token = ref('');

const dialog = ref(false);
const editedLocation = ref('');
const editedCommunicatorName = ref('');
const editedCommunicatorPhoneNumber = ref('');
const editedKeyword = ref('');
const editField = ref('');
const editedState = ref('');
const organization = ref('');
const keywords = ref([]);

const stateOptions = Object.values(stateEnum);

path.value = useTokenData().path.value;
username.value = useTokenData().username.value;
isDispatcher.value = useTokenData().isDispatcher.value;
organization.value = useTokenData().organization.value;
color.value = useTokenData().color.value;
token.value = useTokenData().token.value;

const loadKeywords = async (filePath) => {
  const { data } = await useFetch(filePath).get().text();

  Papa.parse(data.value, {
    header: true,
    complete: (results) => {
      keywords.value = results.data.map(row => row.Stichwort);
    },
    error: (error) => {
      console.error('Error parsing CSV file:', error);
    }
  });
};

if (organization.value === 'Feuerwehr') {
  loadKeywords('/datafiles/emergency/fireEmergencyKeywords.csv');
} else {
  loadKeywords('/datafiles/emergency/policeEmergencyKeywords.csv');
}

const scrollToBottom = () => {
  nextTick(() => {
    if (bottomOfMessages.value) {
      bottomOfMessages.value.scrollIntoView({ behavior: 'smooth' });
    }
  });
};

const openEditDialog = (field) => {
  editField.value = field;
  switch (field) {
    case 'location':
      editedLocation.value = emergencyData.value.value0.location;
      break;
    case 'communicatorName':
      editedCommunicatorName.value = emergencyData.value.value0.communicatorName;
      break;
    case 'communicatorPhoneNumber':
      editedCommunicatorPhoneNumber.value = emergencyData.value.value0.communicatorPhoneNumber;
      break;
    case 'keyword':
      editedKeyword.value = emergencyData.value.value0.keyword;
      break;
    case 'state':
      editedState.value = emergencyData.value.value0.emergencyCallState.emergencyCallStateEnum;
      break;
  }
  dialog.value = true;
};

const confirmEdit = async () => {
  let field;
  let value;
  let number;
  let systemMessage;

  switch (editField.value) {
    case 'location':
      systemMessage = `'${username.value}' hat die Einsatzadresse von '${emergencyData.value.value0.location}' auf '${editedLocation.value}' geändert!`;
      field = 'location';
      value = editedLocation.value;
      number = 1;
      break;
    case 'communicatorName':
      systemMessage = `'${username.value}' hat den Mitteilernamen von '${emergencyData.value.value0.communicatorName}' auf '${editedCommunicatorName.value}' geändert!`;
      field = 'communicatorName';
      value = editedCommunicatorName.value;
      number = 2;
      break;
    case 'communicatorPhoneNumber':
      systemMessage = `'${username.value}' hat die Mitteilerrufnummer von '${emergencyData.value.value0.communicatorPhoneNumber}' auf '${editedCommunicatorPhoneNumber.value}' geändert!`;
      field = 'communicatorPhoneNumber';
      value = editedCommunicatorPhoneNumber.value;
      number = 3;
      break;
    case 'keyword':
      systemMessage = `'${username.value}' hat das Einsatzstichwort von '${emergencyData.value.value0.keyword}' auf '${editedKeyword.value}' geändert!`;
      field = 'keyword';
      value = editedKeyword.value;
      number = 4;
      break;
    case 'state':
      systemMessage = `'${username.value}' hat den Status vom Einsatz von '${emergencyData.value.value0.emergencyCallState.emergencyCallStateEnum}' auf '${editedState.value}' geändert!`;
      field = 'state';
      value = editedState.value;
      number = 5;
      break;
  }

  try {
    const response = await fetch(`/api/v1/emergency/${path.value}/${emergencyData.value.value0.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token.value}`
      },
      body: JSON.stringify({
        number: number,
        value,
        message: systemMessage
      })
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    if (field === 'state')
      emergencyData.value.value0.emergencyCallState.emergencyCallStateEnum = value;
    else
      emergencyData.value.value0[field] = value;

    localStorage.setItem('emergencyData', JSON.stringify(emergencyData.value));
    const jwt = localStorage.getItem('jwt');

    const socketMessage = {
      emergencyId: emergencyData.value.value0.id,
      text: systemMessage,
      dispatcherName: 'system',
      timestamp: new Date().toISOString(),
      jwt: jwt
    };
    socket.send(JSON.stringify(socketMessage));
  } catch (error) {
    console.log(`${field} update failed: ` + error)
  }
  dialog.value = false;
};

const cancelEdit = () => {
  dialog.value = false;
};

async function fetchData() {
  const localstoragePath = localStorage.getItem('path');
  const localstorageId = localStorage.getItem('id');

  const response = await fetch(`/api/v1/emergency/${localstoragePath}/${localstorageId}`, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${token.value}`,
      'Content-Type': 'application/json'
    }
  });

  localStorage.removeItem('path');
  localStorage.removeItem('id');

  const routingResult = await response.json();
  localStorage.setItem('emergencyData', JSON.stringify(routingResult.data));

  emergencyData.value = routingResult.data;

  if (emergencyData.value && emergencyData.value.value1) {
    messages.value = emergencyData.value.value1.map((msg) => ({
      text: msg.text,
      dispatcherName: msg.dispatcherName,
      timestamp: msg.timestamp,
    })).sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
  }
}

onMounted(async () => {
  const storedData = localStorage.getItem('emergencyData');

  if (!storedData) {
    await fetchData();
  } else {
    emergencyData.value = JSON.parse(storedData);

    if (emergencyData.value && emergencyData.value.value1) {
      messages.value = emergencyData.value.value1.map((msg) => ({
        text: msg.text,
        dispatcherName: msg.dispatcherName,
        timestamp: msg.timestamp,
      })).sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
    }
  }

  try {
    const backendIp = import.meta.env.VITE_BACKEND_IP;
    const backendPort = import.meta.env.VITE_BACKEND_PORT;

    socket = new WebSocket(`ws://${backendIp}:${backendPort}/ws?jwt=${token.value}&id=${emergencyData.value.value0.id}`);

    socket.onopen = () => {
      console.log("WebSocket connection established.");
    };

    socket.onmessage = (event) => {
      const messageData = JSON.parse(event.data);
      messages.value.push(messageData);
      messages.value.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
      scrollToBottom();
    };

    socket.onclose = () => {
      console.log("WebSocket connection closed.");
    };

    socket.onerror = (error) => {
      console.error("WebSocket error:", error);
    };
  } catch (error) {
    console.error("WebSocket connection failed:", error);
  }
  scrollToBottom();
  window.addEventListener('beforeunload', handleBeforeUnload);
});

function handleBeforeUnload() {
  localStorage.removeItem('emergencyData');
  localStorage.setItem('path', path.value);
  localStorage.setItem('id', emergencyData.value.value0.id);
}

onBeforeUnmount(() => {
  if (socket) {
    socket.close();
  }
  localStorage.removeItem('emergencyData');
  window.removeEventListener('beforeunload', handleBeforeUnload);
});

watch(messages, (newMessages, oldMessages) => {
  if (newMessages.length !== oldMessages.length) {
    scrollToBottom();
  }
});

const formatTimestamp = (timestamp) => {
  const date = new Date(timestamp);
  const formattedDate = date.toLocaleDateString('de-DE', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  });
  const formattedTime = date.toLocaleTimeString('de-DE', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  });
  return `${formattedTime} ${formattedDate}`;
};

const sendMessage = async () => {
  if (!newMessage.value) {
    alert("Nachricht darf nicht leer sein!");
    return;
  }

  try {
    const jwt = localStorage.getItem('jwt');

    if (!jwt) {
      console.error('JWT not found in localStorage');
      return;
    }

    const response = await fetch(`/api/v1/emergency/${path.value}/message`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${jwt}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        emergencyId: emergencyData.value.value0.id,
        message: newMessage.value
      })
    });

    if (!response.ok) {
      console.error('Failed to send message', response.statusText);
      return;
    }

    const socketMessage = {
      emergencyId: emergencyData.value.value0.id,
      text: newMessage.value,
      dispatcherName: username.value,
      timestamp: new Date().toISOString(),
      jwt: jwt
    };
    socket.send(JSON.stringify(socketMessage));
    newMessage.value = '';
    scrollToBottom();
  } catch (error) {
    console.error('Error sending message', error);
  }
};

function formatMessageText(text) {
  return text.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br>");
}

function formatCursiveMessageText(text) {
  let formatedText = '<i>';
  formatedText += text.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br>");
  formatedText += '</i>';
  return formatedText;
}
</script>

<template>
  <v-app class="main-content">
    <Header :componentName="t('emergencyDetailsTitle')"/>
    <v-container class="content-container">
      <v-card>
        <v-card-text>
          <div v-if="emergencyData" class="emergencyData">
            <v-row>
              <v-col cols="4">
                <v-list dense>
                  <v-list-item v-if="isDispatcher" @click="() => openEditDialog('keyword')" style="cursor: pointer;">
                    <v-icon>mdi-alert</v-icon>
                    {{ emergencyData.value0.keyword }}
                  </v-list-item>
                  <v-list-item v-else>
                    <v-icon>mdi-alert</v-icon>
                    {{ emergencyData.value0.keyword }}
                  </v-list-item>
                  <v-list-item v-if="isDispatcher" @click="() => openEditDialog('location')" style="cursor: pointer;">
                    <v-icon>mdi-map-marker</v-icon>
                    {{ emergencyData.value0.location }}
                  </v-list-item>
                  <v-list-item v-else>
                    <v-icon>mdi-map-marker</v-icon>
                    {{ emergencyData.value0.location }}
                  </v-list-item>
                  <v-list-item>
                    <v-icon>mdi-information</v-icon>
                    {{ emergencyData.value0.information }}
                  </v-list-item>
                </v-list>
              </v-col>
              <v-col cols="4">
                <v-list dense>
                  <v-list-item>
                    <v-icon>mdi-clock</v-icon>
                    {{ formatTimestamp(emergencyData.value0.timestamp) }}
                  </v-list-item>
                  <v-list-item v-if="isDispatcher" @click="() => openEditDialog('state')" style="cursor: pointer;">
                    <p v-if="emergencyData.value0.emergencyCallState.emergencyCallStateEnum === stateEnum.CREATED"><v-icon>mdi-timelapse</v-icon> {{t('CREATED')}}</p>
                    <p v-else-if="emergencyData.value0.emergencyCallState.emergencyCallStateEnum === stateEnum.DISPATCHED"><v-icon>mdi-timelapse</v-icon> {{t('DISPATCHED')}}</p>
                    <p v-else-if="emergencyData.value0.emergencyCallState.emergencyCallStateEnum === stateEnum.RUNNING"><v-icon >mdi-timelapse</v-icon> {{t('RUNNING')}}</p>
                    <p v-else-if="emergencyData.value0.emergencyCallState.emergencyCallStateEnum === stateEnum.COMPLETED"><v-icon>mdi-timelapse</v-icon> {{t('COMPLETED')}}</p>
                    <p v-else><v-icon>mdi-timelapse</v-icon> {{t('FINISHED')}}</p>
                  </v-list-item>
                  <v-list-item v-else>
                    <p v-if="emergencyData.value0.emergencyCallState.emergencyCallStateEnum === stateEnum.CREATED"><v-icon>mdi-timelapse</v-icon> {{t('CREATED')}}</p>
                    <p v-else-if="emergencyData.value0.emergencyCallState.emergencyCallStateEnum === stateEnum.DISPATCHED"><v-icon>mdi-timelapse</v-icon> {{t('DISPATCHED')}}</p>
                    <p v-else-if="emergencyData.value0.emergencyCallState.emergencyCallStateEnum === stateEnum.RUNNING"><v-icon >mdi-timelapse</v-icon> {{t('RUNNING')}}</p>
                    <p v-else-if="emergencyData.value0.emergencyCallState.emergencyCallStateEnum === stateEnum.COMPLETED"><v-icon>mdi-timelapse</v-icon> {{t('COMPLETED')}}</p>
                    <p v-else><v-icon>mdi-timelapse</v-icon> {{t('FINISHED')}}</p>
                  </v-list-item>
                  <v-list-item v-if="isDispatcher" @click="() => openEditDialog('communicatorName')" style="cursor: pointer;">
                    <v-icon>mdi-account</v-icon>
                    {{ emergencyData.value0.communicatorName }}
                  </v-list-item>
                  <v-list-item v-else>
                    <v-icon>mdi-account</v-icon>
                    {{ emergencyData.value0.communicatorName }}
                  </v-list-item>
                  <v-list-item v-if="isDispatcher" @click="() => openEditDialog('communicatorPhoneNumber')" style="cursor: pointer;">
                    <v-icon>mdi-phone</v-icon>
                    {{ emergencyData.value0.communicatorPhoneNumber }}
                  </v-list-item>
                  <v-list-item v-else>
                    <v-icon>mdi-phone</v-icon>
                    {{ emergencyData.value0.communicatorPhoneNumber }}
                  </v-list-item>
                </v-list>
              </v-col>
              <v-col cols="4">
                <v-list dense>
                  <MapComponent :location="emergencyData.value0.location" :height="200" :width="'100%'"/>
                </v-list>
              </v-col>
            </v-row>
          </div>
          <div v-else>
            No emergency data available.
          </div>
        </v-card-text>
      </v-card>
      <v-card>
        <v-card-text class="scrollable-box">
          <v-list dense>
            <v-list-item
                v-for="message in messages"
                :key="message.timestamp"
                class="mb-4"
            >
              <v-list-item-title v-if="message.dispatcherName === 'system'" v-html="formatCursiveMessageText(message.text)" class="message-text-system"/>
              <v-list-item-title v-else v-html="formatMessageText(message.text)" class="message-text"/>
              <v-list-item-subtitle>{{ message.dispatcherName }} - {{
                  formatTimestamp(message.timestamp)
                }}
              </v-list-item-subtitle>
            </v-list-item>
            <div ref="bottomOfMessages"></div>
          </v-list>
        </v-card-text>
      </v-card>
      <v-card v-if="isDispatcher" class="message-box">
        <v-card-text>
          <v-textarea
              v-model="newMessage"
              label="Neue Bemerkung..."
              rows="4"
              variant="outlined"
              full-width
              placeholder="Neue Bemerkung..."
              class="mb-2 textarea-box"
          />
          <v-btn :color="color" @click="sendMessage">{{ t('buttonSend') }}</v-btn>
        </v-card-text>
      </v-card>
    </v-container>
    <v-dialog v-model="dialog" persistent max-width="600px">
      <v-card>
        <v-card-title v-if="editField === 'location'" class="headline">{{ t('updateLocationTitle') }}</v-card-title>
        <v-card-title v-if="editField === 'communicatorName'" class="headline">{{ t('updateCommunicatorNameTitle') }}</v-card-title>
        <v-card-title v-if="editField === 'communicatorPhoneNumber'" class="headline">{{ t('updateCommunicatorPhoneNumberTitle') }}</v-card-title>
        <v-card-title v-if="editField === 'keyword'" class="headline">{{ t('updateEmergencyKeywordTitle') }}</v-card-title>
        <v-card-title v-if="editField === 'state'" class="headline">{{ t('updateEmergencyStateTitle') }}</v-card-title>
        <v-card-text>
          <v-text-field v-if="editField === 'location'" v-model="editedLocation" :label="t('emergencyLocationTitle')"></v-text-field>
          <v-text-field v-if="editField === 'communicatorName'" v-model="editedCommunicatorName" :label="t('communicatorName')"></v-text-field>
          <v-text-field v-if="editField === 'communicatorPhoneNumber'" v-model="editedCommunicatorPhoneNumber" :label="t('communicatorPhoneNumber')"></v-text-field>
          <v-autocomplete v-if="editField === 'keyword'" v-model="editedKeyword" :label="t('emergencyKeywordTitle')" :items="keywords"></v-autocomplete>
          <v-autocomplete v-if="editField === 'state'" v-model="editedState" :label="t('emergencyStateTitle')" :items="stateOptions"></v-autocomplete>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn class="dialog-button-cancel" text @click="cancelEdit">{{ t('buttonCancel') }}</v-btn>
          <v-btn class="dialog-button-save" text @click="confirmEdit">{{ t('buttonSave') }}</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <Footer/>
  </v-app>
</template>

<style scoped>
.content-container {
  padding-top: 80px;
  padding-bottom: 3rem;
  background-color: var(--background-color);
}

.mb-4 {
  margin-bottom: 1rem;
}

.scrollable-box {
  max-height: 300px;
  overflow-y: auto;
  background-color: var(--background-emergency-detail-color);
  color: var(--text-emergency-detail-color);
}

.v-card {
  background-color: var(--background-emergency-detail-color);
  color: var(--text-emergency-detail-color);
}

.main-content{
  background-color: var(--background-color);
}

.emergencyData{
  background-color: var(--background-emergency-detail-color);
  color: var(--text-emergency-detail-color);
}

.message-box{
  background-color: var(--background-emergency-detail-color);
  color: var(--text-emergency-detail-color);
}

.textarea-box {
  background-color: var(--background-emergency-detail-color) !important;
  color: var(--text-emergency-detail-color) !important;
}

.v-list-item{
  background-color: var(--background-emergency-detail-color);
  color: var(--text-emergency-detail-color);
}

.v-list{
  background-color: var(--background-emergency-detail-color);
  color: var(--text-emergency-detail-color);
}

.dialog-button-save{
  color: var(--dialog-button-save-color);
}

.dialog-button-cancel{
  color: var(--dialog-button-cancel-color);
}

.message-text {
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.message-text-system {
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow-wrap: break-word;
  color: var(--color-mode);
}
</style>

<style>
:root {
  --background-color-light: white;
  --background-color-dark: #181818;
  --color-light: rgba(0, 0, 0, 0.45);
  --color-dark: rgba(198, 198, 198, 0.9);
  --background-color: white;
  --dialog-button-cancel-color-light: red;
  --dialog-button-cancel-color-dark: #a10000;
  --dialog-button-save-color-light: green;
  --dialog-button-save-color-dark: #52fb52;
}

[data-theme="light"] {
  --background-emergency-detail-color: white;
  --text-emergency-detail-color: black;
  --background-color: var(--background-color-light);
  --dialog-button-cancel-color: var(--dialog-button-cancel-color-light);
  --dialog-button-save-color: var(--dialog-button-save-color-light);
  --color-mode: var(--color-light)
}

[data-theme="dark"] {
  --background-emergency-detail-color: #8e8e8e;
  --text-emergency-detail-color: white;
  --background-color: var(--background-color-dark);
  --dialog-button-cancel-color: var(--dialog-button-cancel-color-dark);
  --dialog-button-save-color: var(--dialog-button-save-color-dark);
  --color-mode: var(--color-dark)
}
</style>