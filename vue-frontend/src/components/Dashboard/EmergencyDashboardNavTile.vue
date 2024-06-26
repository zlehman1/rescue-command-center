<script setup>
import router from "../../router/index.js";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

function handleCardClick() {
  router.push({name: 'EmergencyDashboard'});
};
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="4">
        <v-card
            @click="handleCardClick()"
            height="100px"
        >
          <v-card-title class="text-center">
            {{ t('information') }}
          </v-card-title>
          <v-card-subtitle class="text-center">
            {{ t('emergencyDashboardSubtitle') }}
          </v-card-subtitle>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.text-center {
  text-align: center;
}
</style>