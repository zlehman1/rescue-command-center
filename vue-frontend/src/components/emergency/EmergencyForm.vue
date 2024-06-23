<script setup>
import { ref } from 'vue';
import Header from "../menu/Header.vue";
import { useI18n } from 'vue-i18n';
import { useTokenData } from '../../composables/useTokenData.js';
import Papa from 'papaparse';
import { useFetch } from '@vueuse/core';
import EmergencyDetails from './EmergencyDetails.vue';
import { useRouter } from 'vue-router';

const { t } = useI18n();
const router = useRouter();

const keyword = ref('');
const location = ref('');
const text = ref('');
const communicatorName = ref('');
const communicatorPhoneNumber = ref('');
let tokenData;
const organization = ref('');
const keywords = ref([]);
const path = ref([]);
const emergencyData = ref(null);

try {
  tokenData = useTokenData();
  organization.value = tokenData.organization.value;
  path.value = tokenData.path.value;
} catch (error) {
  console.error(error.message);
}

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

const sendEmergencyRequest = async () => {
  const url = `/api/v1/emergency/${path.value}`;
  const token = localStorage.getItem('jwt');

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({
        keyword: keyword.value,
        location: location.value,
        information: text.value,
        communicatorName: communicatorName.value,
        communicatorPhoneNumber: communicatorPhoneNumber.value
      })
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    emergencyData.value = result.data;
    const routingResponse = await fetch(`/api/v1/emergency/${path.value}/${emergencyData.value.id}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    const routingResult = await routingResponse.json();
    localStorage.setItem('emergencyData', JSON.stringify(routingResult.data));
    router.push({ name: 'EmergencyDetails' });
  } catch (error) {
    console.error('Error sending request:', error);
  }
};
</script>

<template>
  <v-app>
    <Header :componentName="t('emergencyFormTitle')" />
    <v-main>
      <v-container>
        <v-form>
          <v-autocomplete
              v-model="keyword"
              :items="keywords"
              label="Einsatzstichwort"
              required
          ></v-autocomplete>

          <v-text-field
              v-model="location"
              label="Ort"
              required
          ></v-text-field>

          <v-textarea
              v-model="text"
              label="Einsatztext"
              required
          ></v-textarea>

          <v-text-field
              v-model="communicatorName"
              label="Name des Mitteilers"
              required
          ></v-text-field>

          <v-text-field
              v-model="communicatorPhoneNumber"
              label="Rufnummer des Mitteilers"
              required
              type="tel"
          ></v-text-field>

          <v-btn @click="sendEmergencyRequest">
            Send Request
          </v-btn>
        </v-form>
        <EmergencyDetails v-if="emergencyData" :emergencyData="emergencyData" /> <!-- Display the emergency details if available -->
      </v-container>
    </v-main>
  </v-app>
</template>

<style scoped>
</style>