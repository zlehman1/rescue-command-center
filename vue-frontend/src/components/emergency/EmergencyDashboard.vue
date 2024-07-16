<script setup>
import Header from "../menu/Header.vue";
import {ref, onMounted} from 'vue';
import {useI18n} from 'vue-i18n';
import {useTokenData} from '../../composables/useTokenData.js';
import { useRouter } from 'vue-router';
import { stateEnum } from "../../composables/stateEnum.js";
import Footer from "../../components/menu/Footer.vue";

const {t} = useI18n();
const router = useRouter();
const emergencies = ref([]);
const path = ref('');

try {
  const tokenData = useTokenData();
  path.value = tokenData.path.value;
} catch (error) {
  console.error(error.message);
}

const fetchEmergencies = async () => {
  try {
    const token = localStorage.getItem('jwt');
    const response = await fetch(`/api/v1/emergency/${path.value}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    const data = await response.json();
    emergencies.value = data.data;
  } catch (error) {
    console.error('Error fetching emergencies:', error);
  }
};

const handleCardClick = async (emergency) => {
  const token = localStorage.getItem('jwt');
  const response = await fetch(`/api/v1/emergency/${path.value}/${emergency.id}`, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });

  const result = await response.json();
  localStorage.setItem('emergencyData', JSON.stringify(result.data));
  router.push({name: 'EmergencyDetails'});
};

onMounted(() => {
  fetchEmergencies();
});
</script>

<template>
  <v-app>
    <Header :componentName="t('emergencyDashboardTitle')"/>
    <v-main class="main-content">
      <v-container>
        <v-row>
          <v-col
              v-for="emergency in emergencies"
              :key="emergency.id"
              cols="12"
              md="6"
          >
            <v-card
                class="d-flex flex-column fill-height"
                @click="handleCardClick(emergency)"
            >
              <v-card-title>
                {{ emergency.keyword }} - {{ emergency.location }}
              </v-card-title>
              <v-card-subtitle>
                <p v-if="emergency.emergencyCallState.emergencyCallStateEnum == stateEnum.CREATED">
                  {{ new Date(emergency.timestamp).toLocaleString() }} -
                  {{ t('CREATED') }} - {{ emergency.dispatcherUsername }}
                </p>
                <p v-else-if="emergency.emergencyCallState.emergencyCallStateEnum == stateEnum.DISPATCHED">
                  {{ new Date(emergency.timestamp).toLocaleString() }} -
                  {{ t('DISPATCHED') }} - {{ emergency.dispatcherUsername }}
                </p>
                <p v-else-if="emergency.emergencyCallState.emergencyCallStateEnum == stateEnum.RUNNING">
                  {{ new Date(emergency.timestamp).toLocaleString() }} -
                  {{ t('RUNNING') }} - {{ emergency.dispatcherUsername }}
                </p>
                <p v-else-if="emergency.emergencyCallState.emergencyCallStateEnum == stateEnum.COMPLETED">
                  {{ new Date(emergency.timestamp).toLocaleString() }} -
                  {{ t('COMPLETED') }} - {{ emergency.dispatcherUsername }}
                </p>
                <p v-else-if="emergency.emergencyCallState.emergencyCallStateEnum == stateEnum.FINISHED">
                  {{ new Date(emergency.timestamp).toLocaleString() }} -
                  {{ t('FINISHED') }} - {{ emergency.dispatcherUsername }}
                </p>
              </v-card-subtitle>
              <v-card-text>
                <p>
                  <v-icon>mdi-information</v-icon>
                  {{ emergency.information }}
                </p>
                <br>
                <p>
                  <v-icon>mdi-account</v-icon>
                  {{ emergency.communicatorName }}
                </p>
                <p>
                  <v-icon>mdi-phone</v-icon>
                  {{ emergency.communicatorPhoneNumber }}
                </p>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
    <Footer/>
  </v-app>
</template>

<style scoped>
.v-card {
  min-height: 200px;
  cursor: pointer;
}

.main-content {
  padding-bottom: 3rem;
}
</style>