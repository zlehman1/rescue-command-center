<script setup>
import { ref } from 'vue';
import { VCard, VCardText, VCardActions, VAvatar, VIcon, VListItem, VListItemTitle } from 'vuetify/components';
import { useTokenData } from '../../composables/useTokenData.js';
import { useI18n } from 'vue-i18n';
import router from "../../router/index.js";

const { t } = useI18n();
const profileCardVisible = ref(false);

let tokenData;
const username = ref('');

try {
  tokenData = useTokenData();
  username.value = tokenData.username.value;
} catch (error) {
  console.error(error.message);
}

const logout = () => {
  localStorage.removeItem('jwt');
  localStorage.removeItem('emergencyData');
  window.location.reload();
};

const settingsHandler = () => {
  router.push({name: 'Setting'});
};

const toggleProfileCard = () => {
  profileCardVisible.value = !profileCardVisible.value;
};

defineExpose({
  toggleProfileCard,
  profileCardVisible
});
</script>

<template>
  <v-card v-if="profileCardVisible" class="profile-card" style="position: absolute; top: 60px; right: 20px; width: 300px;">
    <v-card-text class="d-flex align-center">
      <v-avatar class="mr-3" size="40">
        <v-icon>mdi-account-circle</v-icon>
      </v-avatar>
      <span class="username-text">{{ username }}</span>
    </v-card-text>
    <v-card-actions class="actions-container">
      <v-list-item @click="settingsHandler" class="action-item" style="cursor: pointer;">
        <v-list-item-title>{{ t('settings') }}</v-list-item-title>
      </v-list-item>
      <v-list-item @click="logout" class="action-item" style="cursor: pointer;">
        <v-list-item-title>{{ t('logoutTitle') }}</v-list-item-title>
      </v-list-item>
    </v-card-actions>
  </v-card>
</template>

<style scoped>
.profile-card {
  z-index: 1000;
}

.username-text {
  font-weight: bold;
  font-size: 16px;
  color: black;
}

.actions-container {
  display: flex;
  flex-direction: column;
}

.action-item {
  display: flex;
  width: 100%;
}
</style>