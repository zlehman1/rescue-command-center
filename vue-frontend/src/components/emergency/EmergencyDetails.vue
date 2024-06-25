<script setup>
import { onMounted, ref, onBeforeUnmount, nextTick, watch } from 'vue';
import Header from "../menu/Header.vue";
import { useI18n } from "vue-i18n";
import { VIcon, VList, VListItem, VBtn, VContainer, VCard, VCardText, VRow, VCol, VTextarea } from "vuetify/components";
import Footer from "../../components/menu/Footer.vue";
import { useTokenData } from "../../composables/useTokenData.js";
import MapComponent from "../Map/MapComponent.vue";

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

path.value = useTokenData().path.value;
username.value = useTokenData().username.value;
isDispatcher.value = useTokenData().isDispatcher.value;
color.value = useTokenData().color.value;

const scrollToBottom = () => {
  nextTick(() => {
    if (bottomOfMessages.value) {
      bottomOfMessages.value.scrollIntoView({ behavior: 'smooth' });
    }
  });
};

onMounted(() => {
  const storedData = localStorage.getItem('emergencyData');
  if (storedData) {
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
    socket = new WebSocket('ws://localhost:9191/ws');

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
});

onBeforeUnmount(() => {
  if (socket) {
    socket.close();
  }
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
      timestamp: new Date().toISOString()
    };
    socket.send(JSON.stringify(socketMessage));
    newMessage.value = '';
    scrollToBottom();
  } catch (error) {
    console.error('Error sending message', error);
  }
};
</script>

<template>
  <v-app>
    <Header :componentName="t('emergencyDetailsTitle')"/>
    <v-container class="content-container">
      <v-card>
        <v-card-text>
          <div v-if="emergencyData">
            <v-row>
              <v-col cols="4">
                <v-list dense>
                  <v-list-item>
                    <v-icon>mdi-alert</v-icon>
                    {{ emergencyData.value0.keyword }} - {{ emergencyData.value0.emergencyCallState.emergencyCallStateEnum }}
                  </v-list-item>
                  <v-list-item>
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
                  <v-list-item>
                    <v-icon>mdi-account</v-icon>
                    {{ emergencyData.value0.communicatorName }}
                  </v-list-item>
                  <v-list-item>
                    <v-icon>mdi-phone</v-icon>
                    {{ emergencyData.value0.communicatorPhoneNumber }}
                  </v-list-item>
                </v-list>
              </v-col>
              <v-col cols="4">
                <v-list dense>
                  <MapComponent :location="emergencyData.value0.location" :height="200" :width="400" />
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
              <v-list-item-title>{{ message.text }}</v-list-item-title>
              <v-list-item-subtitle>{{ message.dispatcherName }} - {{
                  formatTimestamp(message.timestamp)
                }}
              </v-list-item-subtitle>
            </v-list-item>
            <div ref="bottomOfMessages"></div>
          </v-list>
        </v-card-text>
      </v-card>
      <v-card v-if="isDispatcher">
        <v-card-text>
          <v-textarea
              v-model="newMessage"
              label="Neue Bemerkung..."
              rows="4"
              variant="outlined"
              full-width
              placeholder="Neue Bemerkung..."
              class="mb-2"
          />
          <v-btn :color="color" @click="sendMessage">{{ t('buttonSend') }}</v-btn>
        </v-card-text>
      </v-card>
    </v-container>
    <Footer/>
  </v-app>
</template>

<style scoped>
.content-container {
  padding-top: 80px;
  padding-bottom: 3rem;
}

.mb-4 {
  margin-bottom: 1rem;
}

.scrollable-box {
  max-height: 300px;
  overflow-y: auto;
}
</style>