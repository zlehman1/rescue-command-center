<script setup>
import { ref, watch } from 'vue';
import Header from "../menu/Header.vue";
import { useI18n } from 'vue-i18n';
import Footer from "../../components/menu/Footer.vue";
import MapComponent from "./MapComponent.vue";

const { t } = useI18n();
const mapWidth = ref(1200);
const mapHeight = ref(600);
const location = ref('');

const updateMapSize = () => {
  mapWidth.value = window.innerWidth * 0.8;
  mapHeight.value = window.innerHeight * 0.8;
};

</script>

<template>
  <v-app>
    <Header :componentName="t('mapTitle')"/>
    <v-main class="main-content">
      <div class="map-container">
        <v-container>
          <v-row>
            <v-col cols="12">
              <v-text-field
                  v-model="location"
                  :label="t('enterLocationTitle')"
                  outlined
                  dense
              />
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12">
              <MapComponent :height="mapHeight" :width="mapWidth" :location="location" />
            </v-col>
          </v-row>
        </v-container>
      </div>
    </v-main>
    <Footer/>
  </v-app>
</template>

<style scoped>
.main-content {
  padding-top: 4rem;
  padding-bottom: 3rem;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: calc(100vh - 8rem);
}

.map-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.v-text-field{
  background-color: var(--background-map-color);
  color: var(--text-map-color);
}
</style>

<style>
[data-theme="light"] {
  --background-map-color: white;
  --text-map-color: black;
}

[data-theme="dark"] {
  --background-map-color: #8e8e8e;
  --text-map-color: white;
}
</style>