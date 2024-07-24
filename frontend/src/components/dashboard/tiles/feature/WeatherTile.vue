<script>
import { useTokenData } from '../../../../composables/useTokenData.js';
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { useI18n } from "vue-i18n";

const apiKey = 'c36abce7f0c3f9ab3d85773f9c000959';

export default {
  name: 'WeatherCard',
  setup() {
    const { t } = useI18n();
    const username = ref('');
    const district = ref('');
    const tokenData = useTokenData();
    const loading = ref(true);

    username.value = tokenData.username.value;
    district.value = tokenData.district.value;

    const weatherData = ref({
      location: '',
      description: '',
      temperature: 0,
      humidity: 0,
      condition: ''
    });

    const weatherIcon = computed(() => {
      switch (weatherData.value.condition) {
        case 'Clear':
          return 'mdi-weather-sunny';
        case 'Rain':
          return 'mdi-weather-rainy';
        case 'Clouds':
          return 'mdi-weather-cloudy';
        case 'Snow':
          return 'mdi-weather-snowy';
        default:
          return 'mdi-weather-partly-cloudy';
      }
    });

    const fetchCoordinates = async () => {
      try {
        const response = await axios.get(`http://api.openweathermap.org/geo/1.0/direct?q=${district.value}&limit=1&appid=${apiKey}`);
        if (response.data.length > 0) {
          const { lat, lon } = response.data[0];
          fetchWeatherData(lat, lon);
        } else {
          console.error('No coordinates found for the specified district.');
          loading.value = false;
        }
      } catch (error) {
        console.error('Error fetching coordinates:', error);
        loading.value = false;
      }
    };

    const fetchWeatherData = async (lat, lon) => {
      try {
        const response = await axios.get(`https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${apiKey}&units=metric`);
        const data = response.data;
        weatherData.value = {
          location: data.name,
          description: data.weather[0].description,
          temperature: data.main.temp,
          humidity: data.main.humidity,
          condition: data.weather[0].main
        };
      } catch (error) {
        console.error('Error fetching weather data:', error);
      } finally {
        loading.value = false;
      }
    };

    onMounted(fetchCoordinates);

    return {
      t,
      username,
      district,
      weatherData,
      weatherIcon,
      loading
    };
  }
};
</script>

<template>
  <v-container>
    <v-row justify="center" align="center">
      <v-col cols="12" md="5">
        <v-card class="d-flex flex-column align-center">
          <v-card-title>
            <v-skeleton-loader class="weather-style" :loading="loading" type="text">
              {{ weatherData.location }}
            </v-skeleton-loader>
          </v-card-title>
          <v-card-subtitle>
            <v-skeleton-loader class="weather-style" :loading="loading" type="text">
              {{ weatherData.description }}
            </v-skeleton-loader>
          </v-card-subtitle>
          <v-card-text>
            <v-row justify="center" align="center">
              <v-col class="d-flex justify-center" cols="12">
                <v-skeleton-loader :loading="loading" type="avatar">
                  <v-icon large>
                    {{ weatherIcon }}
                  </v-icon>
                </v-skeleton-loader>
              </v-col>
              <v-col cols="12">
                <div class="d-flex justify-center">
                  <v-skeleton-loader class="weather-style" :loading="loading" type="text">
                    {{ t('temperature') }}: {{ weatherData.temperature }}°C
                  </v-skeleton-loader>
                </div>
                <div class="d-flex justify-center">
                  <v-skeleton-loader class="weather-style" :loading="loading" type="text">
                    {{ t('humidity') }}: {{ weatherData.humidity }}%
                  </v-skeleton-loader>
                </div>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.v-card {
  text-align: center;
  padding: 20px;
  background-color: var(--background-weather-color);
}
.v-icon {
  font-size: 80px;
  background-color: var(--background-weather-color);
  color: var(--weather-color);
}
.v-col.text-center {
  text-align: center;
}
.weather-style{
  background-color: var(--background-weather-color);
  color: var(--weather-color);
}
</style>

<style>
:root {
  --background-weather-color-light: white;
  --background-weather-color-dark: #8e8e8e;
  --weather-color-light: black;
  --weather-color-dark: white;
}

[data-theme="light"] {
  --background-weather-color: var(--background-weather-color-light);
  --weather-color: var(--weather-color-light);
}

[data-theme="dark"] {
  --background-weather-color: var(--background-weather-color-dark);
  --weather-color: var(--weather-color-dark);
}
</style>