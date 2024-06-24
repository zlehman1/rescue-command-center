<template>
  <div id="map" style="height: 300px;"></div>
</template>

<script>
import L from 'leaflet';
import axios from 'axios';
import 'leaflet/dist/leaflet.css';

export default {
  name: 'MapComponent',
  data() {
    return {
      map: null,
      apiKey: '8632dd4ed2ad48ccb6c52216742b6c88',
      location: ' 46414 Rhede, Germany',
      lat: 52.5200,
      lng: 13.4050
    };
  },
  mounted() {
    this.initializeMap();
    this.getCoordinates();
  },
  methods: {
    initializeMap() {
      this.map = L.map('map').setView([this.lat, this.lng], 13);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      }).addTo(this.map);
    },
    async getCoordinates() {
      try {
        const response = await axios.get(`https://api.opencagedata.com/geocode/v1/json?q=${this.location}&key=${this.apiKey}`);
        const {lat, lng} = response.data.results[0].geometry;
        this.lat = lat;
        this.lng = lng;
        this.updateMap();
      } catch (error) {
        console.error('Error fetching coordinates:', error);
      }
    },
    updateMap() {
      this.map.setView([this.lat, this.lng], 13);
      L.marker([this.lat, this.lng]).addTo(this.map);
    }
  }
};
</script>

<style>
#map {
  width: 100%;
  height: 100%;
}
</style>