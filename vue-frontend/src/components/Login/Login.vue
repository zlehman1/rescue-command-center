<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { VTextField, VBtn, VCard, VCardText, VCardTitle, VContainer, VRow, VCol } from 'vuetify/components'

const username = ref('')
const password = ref('')
const router = useRouter()

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
      throw new Error('Login failed')
    }
    const token = await response.text()
    localStorage.setItem('jwt', token)
    router.push({ name: 'Home' })
  } catch (error) {
    console.error('Error during login:', error)
  }
}

const requiredRule = value => !!value || 'Required.'
</script>

<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card>
          <v-card-title>Login</v-card-title>
          <v-card-text>
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
</template>

<style>
html, body, #app {
  height: 100%;
  margin: 0;
}
</style>