import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import i18n from './i18n';

import { createVuetify } from 'vuetify';
import 'vuetify/lib/styles/main.css';
import '@mdi/font/css/materialdesignicons.css';
import Toast, { POSITION } from 'vue-toastification';
import 'vue-toastification/dist/index.css';

const vuetify = createVuetify();

createApp(App).use(router).use(vuetify).use(i18n).use(Toast, {
    position: POSITION.TOP_RIGHT
}).mount('#app');