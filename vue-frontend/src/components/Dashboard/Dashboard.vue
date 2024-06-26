<script setup>
import Header from '../menu/Header.vue'
import Footer from '../menu/Footer.vue'
import WeatherCard from "./WeatherTile.vue";
import Greeting from "./GreetingTile.vue";
import EmergencyDashboard from "./EmergencyDashboardNavTile.vue";
import EmergencyCreationNavTile from "./EmergencyCreationNavTile.vue";
</script>

<template>
  <v-app>
    <Header componentName="Dashboard" />
    <v-main class="main-content">
      <div class="card-container">
        <Greeting />
        <WeatherCard />
        <EmergencyDashboard/>
        <EmergencyCreationNavTile/>
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

}
</style>