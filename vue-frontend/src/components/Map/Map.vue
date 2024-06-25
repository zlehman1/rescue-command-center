<script setup>
import { ref } from 'vue';
import Header from "../menu/Header.vue";
import { useI18n } from 'vue-i18n';
import Footer from "../../components/menu/Footer.vue";
import MapComponent from "./MapComponent.vue";

const { t } = useI18n();
const mapWidth = ref(window.innerWidth * 0.8);
const mapHeight = ref(window.innerHeight * 0.8);
const location = ref('Düsseldorf, Germany');

const updateMapSize = () => {
  mapWidth.value = window.innerWidth * 0.8;
  mapHeight.value = window.innerHeight * 0.8;
};

window.addEventListener('resize', updateMapSize);
</script>

<template>
  <v-app>
    <Header :componentName="t('mapTitle')"/>
    <v-main class="main-content">
      <div class="map-container">
        <MapComponent :height="mapHeight" :width="mapWidth" :location="location" />
      </div>
    </v-main>
    <Footer/>
  </v-app>
</template>

<style scoped>
.main-content {
  padding-bottom: 3rem;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.map-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
</style>
