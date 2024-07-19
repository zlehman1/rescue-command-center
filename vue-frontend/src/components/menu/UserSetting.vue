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
      <v-container class="form-container">
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
.main-content {
  padding-bottom: 3rem;
  background-color: var(--background-color);
}

.form-container {
  padding: 1rem;
  background-color: var(--background-color);
}

.v-text-field {
  background-color: var(--background-color-card);
  color: var(--text-color);
  margin-bottom: 0;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
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