<script setup>
import Header from "../menu/Header.vue";
import {useI18n} from 'vue-i18n';
import Footer from "../../components/menu/Footer.vue";

const {t} = useI18n();
</script>

<template>
  <v-app>
    <Header :componentName="t('mapTitle')"/>
    <v-main class="main-content"></v-main>
    <Footer/>
  </v-app>
</template>

<style scoped>
.main-content {
  padding-bottom: 3rem;
}
</style>