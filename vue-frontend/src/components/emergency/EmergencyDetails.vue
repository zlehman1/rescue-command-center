<script setup>
import { ref, onMounted } from 'vue';

const emergencyData = ref(null);

onMounted(() => {
  const storedData = localStorage.getItem('emergencyData');
  if (storedData) {
    emergencyData.value = JSON.parse(storedData);
  }
});
</script>

<template>
  <div v-if="emergencyData">
    <h2>Emergency Details</h2>
    <p><strong>ID:</strong> {{ emergencyData.id }}</p>
    <p><strong>Timestamp:</strong> {{ emergencyData.timestamp }}</p>
    <p><strong>Keyword:</strong> {{ emergencyData.keyword }}</p>
    <p><strong>Location:</strong> {{ emergencyData.location }}</p>
    <p><strong>Information:</strong> {{ emergencyData.information }}</p>
    <p><strong>Communicator Name:</strong> {{ emergencyData.communicatorName }}</p>
    <p><strong>Communicator Phone Number:</strong> {{ emergencyData.communicatorPhoneNumber }}</p>
    <p><strong>Dispatcher Username:</strong> {{ emergencyData.dispatcherUsername }}</p>
    <p><strong>Emergency Call State:</strong> {{ emergencyData.emergencyCallState.emergencyCallStateEnum }}</p>
  </div>
</template>

<style scoped>
h2 {
  margin-bottom: 1rem;
}
p {
  margin: 0.5rem 0;
}
</style>