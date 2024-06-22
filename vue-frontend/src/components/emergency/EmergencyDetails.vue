<script setup>
import { onMounted, ref } from 'vue';
import Header from "../menu/Header.vue";
import { useI18n } from "vue-i18n";
import {VIcon} from "vuetify/components";

const { t } = useI18n();
const emergencyData = ref(null);

onMounted(() => {
  const storedData = localStorage.getItem('emergencyData');
  if (storedData) {
    emergencyData.value = JSON.parse(storedData);
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
</script>

<template>
  <v-app>
    <Header :componentName="t('emergencyDetailsTitle')"/>
    <v-container class="content-container">
      <v-card>
        <v-card-text>
          <v-list dense v-if="emergencyData">
            <v-list-item>
              <v-icon>mdi-alert</v-icon> {{ emergencyData.keyword }}
            </v-list-item>
            <v-list-item>
              <v-icon>mdi-map-marker</v-icon> {{ emergencyData.location }}
            </v-list-item>
            <v-list-item>
              <v-icon>mdi-information</v-icon> {{ emergencyData.information }}
            </v-list-item>
            <v-list-item>
              <v-icon>mdi-clock</v-icon> {{ formatTimestamp(emergencyData.timestamp) }}
            </v-list-item>
            <v-list-item>
              <v-icon>mdi-account</v-icon> {{emergencyData.communicatorName}}
            </v-list-item>
            <v-list-item>
              <v-icon>mdi-phone</v-icon> {{ emergencyData.communicatorPhoneNumber }}
            </v-list-item>
            <v-list-item>
              <v-icon>mdi-alert</v-icon> {{emergencyData.emergencyCallState.emergencyCallStateEnum}}
            </v-list-item>
          </v-list>
          <div v-else>
            No emergency data available.
          </div>
        </v-card-text>
      </v-card>
    </v-container>
  </v-app>
</template>

<style scoped>
.content-container {
  padding-top: 80px;
}
</style>