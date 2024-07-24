<script setup>
import {useTokenData} from '../../../../composables/useTokenData.js';
import {useI18n} from 'vue-i18n';
import {ref} from 'vue';

const {t} = useI18n();
const username = ref('');
const organization = ref('');
const tokenData = useTokenData();

username.value = tokenData.username.value;
organization.value = tokenData.organization.value;

const greetingMessage = ref('');

const currentHour = new Date().getHours();
if (currentHour < 12) {
  greetingMessage.value = t('goodMorning');
} else if (currentHour < 18) {
  greetingMessage.value = t('goodAfternoon');
} else {
  greetingMessage.value = t('goodEvening');
}
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="5">
        <v-card class="greeting-tile">
          <v-card-title>
            {{ greetingMessage }}, {{ username }}!
          </v-card-title>
          <v-card-text v-if="organization === 'Feuerwehr'">
            {{ t('welcomeMessageFire') }}
          </v-card-text>
          <v-card-text v-else-if="organization === 'Polizei'">
            {{ t('welcomeMessagePolice') }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style>
[data-theme="light"] {
  --background-greeting-color: white;
  --text-greeting-color: black;
}

[data-theme="dark"] {
  --background-greeting-color: #8e8e8e;
  --text-greeting-color: white;
}

.greeting-tile {
  text-align: center;
  padding: 20px;
  background-color: var(--background-greeting-color);
  color: var(--text-greeting-color);
}
</style>