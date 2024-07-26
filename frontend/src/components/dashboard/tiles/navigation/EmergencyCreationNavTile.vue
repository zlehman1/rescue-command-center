<script setup>
import router from "../../../../router/index.js";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

function handleCardClick() {
  router.push({name: 'EmergencyForm'});
};
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="4">
        <v-card
            @click="handleCardClick()"
            height="100px"
            class="creation-nav-tile"
        >
          <v-card-title class="text-center">
            {{ t('emergencyFormTitle') }}
          </v-card-title>
          <v-card-subtitle class="text-center">
            {{ t('emergencyFormSubtitle') }}
          </v-card-subtitle>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
[data-theme="light"] {
  --background-greeting-color: white;
  --text-greeting-color: black;
}

[data-theme="dark"] {
  --background-greeting-color: #8e8e8e;
  --text-greeting-color: white;
}

.creation-nav-tile{
  background-color: var(--background-greeting-color);
  color: var(--text-greeting-color);
}

.text-center {
  text-align: center;
}
</style>