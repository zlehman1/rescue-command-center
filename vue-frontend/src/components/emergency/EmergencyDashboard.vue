<script setup>
import Header from "../menu/Header.vue";
import {useI18n} from 'vue-i18n';

const {t} = useI18n();
</script>

<template>
  <v-app>
    <Header :componentName="t('emergencyDashboardTitle')"/>
  </v-app>
</template>

<style scoped>

</style>