<script setup>
import Footer from "../menu/Footer.vue";
import Header from "../menu/Header.vue";
import { useI18n } from "vue-i18n";
import { useRouter } from 'vue-router';
import {useTokenData} from '../../composables/useTokenData.js';
import {ref} from "vue";

const { t } = useI18n();
const router = useRouter();
const isAdmin = ref(false);

const tokenData = useTokenData();
isAdmin.value = tokenData.isAdmin.value;

const navigateToUserSettings = () => {
  router.push('/setting/user');
};

const navigateToAdminSettings = () => {
  router.push('/setting/admin');
};
</script>

<template>
  <v-app>
    <Header :componentName="t('settings')" />
    <v-main class="main-content">
      <v-container>
        <v-row>
          <v-col cols="12" md="6">
            <v-card class="d-flex flex-column fill-height" @click="navigateToUserSettings">
              <v-card-title><v-icon>mdi-account</v-icon> {{ t('userSettingsTitle') }}</v-card-title>
              <v-card-subtitle>{{ t('userSettingsSubtitle') }}</v-card-subtitle>
            </v-card>
          </v-col>
          <v-col cols="12" md="6" v-if="isAdmin">
            <v-card class="d-flex flex-column fill-height" @click="navigateToAdminSettings">
              <v-card-title><v-icon>mdi-cog</v-icon> {{ t('adminSettingsTitle') }}</v-card-title>
              <v-card-subtitle>{{ t('adminSettingsSubtitle') }}</v-card-subtitle>
            </v-card>
          </v-col>
        </v-row>
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