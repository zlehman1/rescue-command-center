<script setup>
import { ref, onMounted } from 'vue';
import { VAppBar, VToolbarTitle, VContainer, VIcon, VList, VListItem, VBtn, VCard, VCardText, VCardActions, VRow, VCol, VNavigationDrawer } from 'vuetify/components';
import { useTokenData } from '../../composables/useTokenData.js';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
const props = defineProps({
  componentName: {
    type: String,
    required: true
  }
});

let tokenData;
const backgroundColor = ref('#1976d2'); // Default color
const username = ref('');

try {
  tokenData = useTokenData();
  backgroundColor.value = tokenData.color.value;
  username.value = tokenData.username.value;
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

const logout = () => {
  localStorage.removeItem('jwt');
  localStorage.removeItem('emergencyData');
  window.location.reload();
};

const profileCardVisible = ref(false);
const drawer = ref(false);

const toggleProfileCard = () => {
  profileCardVisible.value = !profileCardVisible.value;
};
</script>

<template>
  <v-navigation-drawer v-model="drawer" app>
    <v-list dense>
      <v-list-item />
      <v-list-item to="/">
        <v-icon>mdi-view-dashboard</v-icon> {{t('dashboardTitle')}}
      </v-list-item>
      <v-list-item to="/emergency/create">
        <v-icon>mdi-bell-plus</v-icon> {{t('emergencyFormTitle')}}
      </v-list-item>
      <v-list-item to="/emergency/overview">
        <v-icon>mdi-eye</v-icon> {{t('emergencyDashboardTitle')}}
      </v-list-item>
      <v-list-item to="/map">
        <v-icon>mdi-map</v-icon> {{t('mapTitle')}}
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
          <v-toolbar-title class="ml-3">{{ componentName }}</v-toolbar-title>
        </v-col>
        <v-col cols="auto">
          {{ currentTime }}
          <v-btn icon @click="toggleProfileCard">
            <v-icon>mdi-account</v-icon>
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-app-bar>

  <v-card v-if="profileCardVisible" class="profile-card" style="position: absolute; top: 60px; right: 20px; width: 300px;">
    <v-card-text class="d-flex align-center">
      <v-avatar class="mr-3" size="40">
        <v-icon>mdi-account-circle</v-icon>
      </v-avatar>
      <span class="username-text">{{ username }}</span>
    </v-card-text>
    <v-card-actions>
      <v-list-item @click="logout" style="width: 100%; cursor: pointer;">
        <v-list-item-title>Abmelden</v-list-item-title>
      </v-list-item>
    </v-card-actions>
  </v-card>
</template>

<style scoped>
.v-app-bar {
  color: white;
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

.profile-card {
  z-index: 1000;
}

.username-text {
  font-weight: bold;
  font-size: 16px;
  color: black;
}
</style>