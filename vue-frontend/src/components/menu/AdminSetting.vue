<script setup>
import { useI18n } from "vue-i18n";
import {useTokenData} from '../../composables/useTokenData.js';
import Footer from "../menu/Footer.vue";
import Header from "../menu/Header.vue";
import {ref} from "vue";

const { t } = useI18n();
const token = ref('');

const tokenData = useTokenData();
token.value = tokenData.token.value;
</script>

<template>
  <v-app>
    <Header :componentName="t('adminSettingsTitle')" />
    <v-main class="main-content">
      <v-container>

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