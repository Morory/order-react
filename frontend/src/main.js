import Vue from 'vue';
import App from './App.vue';
import { router } from './router';
import store from './store';
import VeeValidate from 'vee-validate';
import Vuex from 'vuex';
import vuetify from './plugins/vuetify'
import VueMoment from 'vue-moment'

Vue.config.productionTip = false;

Vue.use(VeeValidate, {
  fieldsBagName: 'vvFields'
});
Vue.use(Vuex);
Vue.use(VueMoment);

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App),
}).$mount('#app');