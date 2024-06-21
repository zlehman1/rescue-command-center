<script setup>
import { ref } from 'vue';
import Header from "../menu/Header.vue";
import { useI18n } from 'vue-i18n';
import { useTokenData } from '../../composables/useTokenData.js';
import Papa from 'papaparse';
import { useFetch } from '@vueuse/core';

const { t } = useI18n();

const keyword = ref('');
const location = ref('');
const text = ref('');
const communicatorName = ref('');
const communicatorPhoneNumber = ref('');
let tokenData;
const organization = ref('');
const keywords = ref([]);

try {
  tokenData = useTokenData();
  organization.value = tokenData.organization.value;
} catch (error) {
  console.error(error.message);
}

/*if (organization.value === 'Feuerwehr') {
  const { data, error } = await useFetch('../../emergency/fireEmergencyKeywords.csv').get().text();
  if (data.value) {
    Papa.parse(data.value, {
      header: true,
      complete: function(results) {
        keywords.value = results.data.map(item => item.Stichwort);
      }
    });
  }
} else {
  // Andere Organisationen verarbeiten
}
*/

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
        </v-form>
      </v-container>
    </v-main>
  </v-app>
</template>

<style scoped>

</style>