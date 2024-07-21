<script setup>
import {onMounted, onUnmounted, ref} from 'vue'
import { useRouter } from 'vue-router'
import { VTextField, VBtn, VCard, VCardText, VCardTitle, VContainer, VRow, VCol, VAlert } from 'vuetify/components'
import {useI18n} from "vue-i18n";
import Footer from "../../components/menu/Footer.vue";

const { t } = useI18n();
const username = ref('')
const password = ref('')
const errorMessage = ref('')
const showError = ref(false)
const router = useRouter()

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

const login = async () => {
  try {
    const response = await fetch('/api/v1/authentication/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ username: username.value, password: password.value })
    })

    if (!response.ok) {
      let errorMessage;
      switch (response.status) {
        case 403:
          errorMessage = t('loginForbidden');
          break;
        default:
          errorMessage = t('loginError');
      }
      throw new Error(errorMessage);
    }

    const token = await response.text()
    localStorage.setItem('jwt', token)
    router.push({ name: 'Home' })
  } catch (error) {
    errorMessage.value = error.message;
    showError.value = true;
    setTimeout(() => {
      showError.value = false;
    }, 10000);
  }
}

const requiredRule = value => !!value || 'Required.'
</script>

<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card class="login-card">
          <v-card-title>Login</v-card-title>
          <v-card-text>
            <v-alert
                v-if="showError"
                type="error"
                dismissible
                prominent
            >
              {{ errorMessage }}
            </v-alert>
            <v-text-field
                label="Username"
                v-model="username"
                prepend-inner-icon="mdi-account"
                type="text"
                @keyup.enter="login"
                :rules="[requiredRule]"
            />
            <v-text-field
                label="Password"
                v-model="password"
                prepend-inner-icon="mdi-lock"
                type="password"
                @keyup.enter="login"
                :rules="[requiredRule]"
            />
            <v-btn color="primary" @click="login" block>Login</v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
  <Footer/>
</template>

<style scoped>
.v-container{
  background-color: var(--login-background-color);
}
.v-card{
  background-color: var(--login-card-background-color);
  color: var(--login-card-text-color);
}
</style>

<style>
:root {
  --login-background-color-light: white;
  --login-text-color-light: black;
  --login-background-color-dark: #181818;
  --login-text-color-dark: white;
  --login-card-background-color-light: white;
  --login-card-text-color-light: black;
  --login-card-background-color-dark: #818181;
  --login-card-text-color-dark: white;
}

[data-theme="light"] {
  --login-background-color: var(--login-background-color-light);
  --login-text-color: var(--login-text-color-light);
  --login-card-background-color: var(--login-card-background-color-light);
  --login-card-text-color: var(--login-card-text-color-light);
}

[data-theme="dark"] {
  --login-background-color: var(--login-background-color-dark);
  --login-text-color: var(--login-text-color-dark);
  --login-card-background-color: var(--login-card-background-color-dark);
  --login-card-text-color: var(--login-card-text-color-dark);
}

html, body, #app {
  height: 100%;
  margin: 0;
}
</style>