<script setup>
import Header from '../menu/Header.vue'
import Footer from '../menu/Footer.vue'
</script>

<template>
  <v-app>
    <Header componentName="Dashboard" />
    <v-main class="main-content"></v-main>
    <Footer/>
  </v-app>
</template>

<style scoped>
.main-content {
  padding-bottom: 3rem;
}
</style>