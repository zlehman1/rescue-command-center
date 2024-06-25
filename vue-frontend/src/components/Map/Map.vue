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

window.addEventListener('resize', updateMapSize);

watch(location, (newLocation) => {
  console.log('Location updated to:', newLocation);
});

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
                  :label="t('Enter Location')"
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
  padding-top: 4rem; /* Füge oben ausreichend Abstand hinzu, um den Header nicht zu überlappen */
  padding-bottom: 3rem;
  display: flex;
  justify-content: center;
  align-items: flex-start; /* Ändere von center zu flex-start, um den Inhalt von oben zu beginnen */
  min-height: calc(100vh - 8rem); /* Passe die minimale Höhe an, um Header und Footer zu berücksichtigen */
}

.map-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
}
</style>