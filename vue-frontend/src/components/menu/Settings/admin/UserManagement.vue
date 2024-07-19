<script setup>
import Footer from "../../Footer.vue";
import Header from "../../Header.vue";
import { useI18n } from "vue-i18n";
import {useTokenData} from '../../../../composables/useTokenData.js';
import {ref} from "vue";

const { t } = useI18n();

const tokenData = useTokenData();
</script>

<template>
  <v-app>
    <Header :componentName="t('userManagementSettingsTitle')" />
    <v-main class="main-content">
      <v-container>

      </v-container>
    </v-main>
    <Footer />
  </v-app>
</template>

<style scoped>
.main-content {
  padding-bottom: 3rem;
  background-color: var(--background-color);
}
</style>

<style>
:root {
  --background-color-light: white;
  --background-color-dark: #181818;
  --background-color-card-light: white;
  --background-color-card-dark: #8e8e8e;
  --text-color-light: black;
  --text-color-dark: white;
}

[data-theme="light"] {
  --text-color: var(--text-color-light);
  --background-color: var(--background-color-light);
  --background-color-card: var(--background-color-card-light);
}

[data-theme="dark"] {
  --text-color: var(--text-color-dark);
  --background-color: var(--background-color-dark);
  --background-color-card: var(--background-color-card-dark);
}
</style>