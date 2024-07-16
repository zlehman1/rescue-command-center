<script setup>
import {ref, onMounted, onBeforeUnmount} from 'vue';
import {
  VAppBar,
  VToolbarTitle,
  VContainer,
  VIcon,
  VList,
  VListItem,
  VBtn,
  VRow,
  VCol,
  VNavigationDrawer
} from 'vuetify/components';
import {useTokenData} from '../../composables/useTokenData.js';
import {darkenColor} from '../../composables/darkenColor.js';
import {useI18n} from 'vue-i18n';
import ProfileCard from './ProfileCard.vue';

const {t} = useI18n();
const props = defineProps({
  componentName: {
    type: String,
    required: true
  }
});

let tokenData;
const backgroundColor = ref('');
const defaultBackgroundColor = ref('');

try {
  tokenData = useTokenData();
  backgroundColor.value = tokenData.color.value;
  defaultBackgroundColor.value = tokenData.color.value;
} catch (error) {
  console.error(error.message);
}

const currentTime = ref('');

const updateTime = () => {
  const now = new Date();
  const day = String(now.getDate()).padStart(2, '0');
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const year = now.getFullYear();
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  currentTime.value = `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
};

onMounted(() => {
  updateTime();
  setInterval(updateTime, 1000);
});

const drawer = ref(false);
const profileCardRef = ref(null);

const toggleProfileCard = () => {
  profileCardRef.value.toggleProfileCard();
};

const isMobile = ref(window.innerWidth <= 768);

const handleResize = () => {
  isMobile.value = window.innerWidth <= 768;
};

onMounted(() => {
  window.addEventListener('resize', handleResize);
  handleResize();
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
});

const isDarkMode = ref(localStorage.getItem('theme') === 'dark');

const toggleDarkMode = () => {
  isDarkMode.value = !isDarkMode.value;
  localStorage.setItem('theme', isDarkMode.value ? 'dark' : 'light');
  document.documentElement.setAttribute('data-theme', isDarkMode.value ? 'dark' : 'light');
  backgroundColor.value = isDarkMode.value ? darkenColor(defaultBackgroundColor.value, 20) : defaultBackgroundColor.value;
};

onMounted(() => {
  const savedTheme = localStorage.getItem('theme');
  if (savedTheme) {
    isDarkMode.value = savedTheme === 'dark';
    document.documentElement.setAttribute('data-theme', savedTheme);
    backgroundColor.value = isDarkMode.value ? darkenColor(defaultBackgroundColor.value, 20) : defaultBackgroundColor.value;
  }
});
</script>

<template>
  <v-navigation-drawer v-model="drawer" app>
    <v-list dense>
      <v-list-item/>
      <v-list-item to="/">
        <v-icon>mdi-view-dashboard</v-icon>
        {{ t('dashboardTitle') }}
      </v-list-item>
      <v-list-item to="/emergency/create">
        <v-icon>mdi-bell-plus</v-icon>
        {{ t('emergencyFormTitle') }}
      </v-list-item>
      <v-list-item to="/emergency/overview">
        <v-icon>mdi-eye</v-icon>
        {{ t('emergencyDashboardTitle') }}
      </v-list-item>
      <v-list-item to="/map">
        <v-icon>mdi-map</v-icon>
        {{ t('mapTitle') }}
      </v-list-item>
    </v-list>
  </v-navigation-drawer>

  <v-app-bar :style="{ backgroundColor: backgroundColor }" app>
    <v-container>
      <v-row align="center" justify="space-between">
        <v-col cols="auto" class="d-flex align-center">
          <v-btn icon @click="drawer = !drawer">
            <v-icon>mdi-menu</v-icon>
          </v-btn>
          <v-toolbar-title v-show="!isMobile" class="ml-3">{{ componentName }}</v-toolbar-title>
        </v-col>
        <v-col cols="auto">
          {{ currentTime }}
          <v-btn icon @click="toggleDarkMode">
            <v-icon>{{ isDarkMode ? 'mdi-weather-night' : 'mdi-white-balance-sunny' }}</v-icon>
          </v-btn>
          <v-btn icon @click="toggleProfileCard">
            <v-icon>mdi-account</v-icon>
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-app-bar>

  <ProfileCard ref="profileCardRef"/>
</template>

<style scoped>
.v-app-bar {
  color: white;
}

[data-theme="light"] {
  --background-color: white;
  --text-color: black;
}

[data-theme="dark"] {
  --background-color: black;
  --text-color: white;
}

.backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  z-index: 999;
}

.v-app {
  background-color: var(--background-color);
  color: var(--text-color);
}
</style>