<script setup>
import { useI18n } from "vue-i18n";
import Header from '../menu/Header.vue'
import Footer from '../menu/Footer.vue'
import WeatherCard from "./tiles/feature/WeatherTile.vue";
import Greeting from "./tiles/feature/GreetingTile.vue";
import EmergencyDashboard from "./tiles/navigation/EmergencyDashboardNavTile.vue";
import EmergencyCreationNavTile from "./tiles/navigation/EmergencyCreationNavTile.vue";

const { t } = useI18n();
</script>

<template>
  <v-app>
    <Header :componentName="t('dashboardTitle')" />
    <v-main class="main-content">
      <div class="card-container">
        <Greeting class="greeting" />
        <WeatherCard class="weather" />
        <EmergencyDashboard class="emergency-dashboard" />
        <EmergencyCreationNavTile class="emergency-creation" />
      </div>
    </v-main>
    <Footer />
  </v-app>
</template>

<style scoped>
.main-content {
  padding-bottom: 3rem;
}

.card-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-gap: 1rem;
}

.greeting {
  grid-column: span 2;
}

.weather {
  grid-column: span 2;
}

.emergency-dashboard, .emergency-creation {
  grid-column: span 1;
}
</style>