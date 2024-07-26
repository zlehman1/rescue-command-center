<script setup>
import { useI18n } from "vue-i18n";
import Header from '../menu/Header.vue'
import Footer from '../menu/Footer.vue'
import WeatherCard from "./tiles/feature/WeatherTile.vue";
import Greeting from "./tiles/feature/GreetingTile.vue";
import EmergencyDashboard from "./tiles/navigation/EmergencyDashboardNavTile.vue";
import EmergencyCreationNavTile from "./tiles/navigation/EmergencyCreationNavTile.vue";
import { ref, onMounted, onUnmounted } from "vue";
import {useTokenData} from "../../composables/useTokenData.js";

const isDispatcher = ref(false);

const tokenData = useTokenData();
isDispatcher.value = tokenData.isDispatcher.value;

const { t } = useI18n();

const isDarkMode = ref(localStorage.getItem('theme') === 'dark');

const setTheme = () => {
  document.documentElement.setAttribute('data-theme', isDarkMode.value ? 'dark' : 'light');
}

const handleStorageChange = (event) => {
  if (event.key === 'theme') {
    isDarkMode.value = event.newValue === 'dark';
    setTheme();
  }
};

onMounted(() => {
  window.addEventListener('storage', handleStorageChange);
  setTheme();
});

onUnmounted(() => {
  window.removeEventListener('storage', handleStorageChange);
});
</script>

<template>
  <v-app>
    <Header :componentName="t('dashboardTitle')" />
    <v-main class="main-content">
      <div class="card-container">
        <Greeting class="greeting" />
        <WeatherCard class="weather" />
        <EmergencyDashboard class="emergency-dashboard" />
        <EmergencyCreationNavTile v-if="isDispatcher" class="emergency-creation" />
      </div>
    </v-main>
    <Footer />
  </v-app>
</template>

<style>
:root {
  --background-color-light: white;
  --background-color-dark: #181818;
}

[data-theme="light"] {
  --background-color: var(--background-color-light);
}

[data-theme="dark"] {
  --background-color: var(--background-color-dark);
}

.main-content {
  padding-bottom: 3rem;
  background-color: var(--background-color);
}

.card-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-gap: 1rem;
}

.greeting {
  grid-column: span 2;
}

.weather {
  grid-column: span 2;
}

.emergency-dashboard, .emergency-creation {
  grid-column: span 1;
}
</style>