<script setup>
import Header from '../menu/Header.vue'
</script>

<template>
  <v-app>
    <Header componentName="Dashboard" />
    <router-view></router-view>
  </v-app>
</template>