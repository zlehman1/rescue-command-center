<script setup>
import { useI18n } from "vue-i18n";
import { useTokenData } from '../../composables/useTokenData.js';
import Footer from "../menu/Footer.vue";
import Header from "../menu/Header.vue";
import { ref } from "vue";

const { t } = useI18n();

const token = ref('');
const color = ref('');
const currentPassword = ref('');
const newPassword = ref('');
const repeatedPassword = ref('');
const showAlert = ref(false);
const alertMessage = ref('');
const alertType = ref(''); // 'success' or 'error'

const tokenData = useTokenData();
token.value = tokenData.token.value;
color.value = tokenData.color.value;

const handleSubmit = async () => {
  if (newPassword.value !== repeatedPassword.value) {
    alertMessage.value = t('passwordsDoNotMatch');
    alertType.value = 'error';
    showAlert.value = true;
    return;
  }

  try {
    const response = await fetch('/api/v1/users/password', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token.value}`
      },
      body: JSON.stringify({
        currentPassword: currentPassword.value,
        newPassword: newPassword.value
      })
    });

    if (!response.ok) {
      alertMessage.value = t('errorUpdatingPassword');
      alertType.value = 'error';
      showAlert.value = true;
      throw new Error(`HTTP error! status: ${response.status}`);
    } else {
      alertMessage.value = t('passwordUpdatedSuccessfully');
      alertType.value = 'success';
      showAlert.value = true;
    }
  } catch (error) {
    alertMessage.value = t('errorUpdatingPassword');
    alertType.value = 'error';
    showAlert.value = true;
    console.error(error);
  }
};
</script>

<template>
  <v-app>
    <Header :componentName="t('userSettingsTitle')" />
    <v-main class="main-content">
      <v-container>
        <v-alert
            v-if="showAlert"
            :type="alertType"
            dismissible
            @input="showAlert = false"
        >
          {{ alertMessage }}
        </v-alert>
        <form @submit.prevent="handleSubmit">
          <v-text-field
              v-model="currentPassword"
              :label="t('currentPassword')"
              required
              type="password"
          />
          <v-text-field
              v-model="newPassword"
              :label="t('newPassword')"
              required
              type="password"
          />
          <v-text-field
              v-model="repeatedPassword"
              :label="t('repeatedPassword')"
              required
              type="password"
          />
          <v-btn type="submit" :color="color">{{ t('buttonSave') }}</v-btn>
        </form>
      </v-container>
    </v-main>
    <Footer />
  </v-app>
</template>

<style scoped>
.v-card {
  min-height: 200px;
  cursor: pointer;
}

.main-content {
  padding-bottom: 3rem;
}
</style>