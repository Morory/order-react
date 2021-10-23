<template>
  <div>
    <v-card elevation="4" class="mx-auto mt-16" max-width="400">
      <v-card-title>
        <h3 class="headline">
          Login
        </h3>
      </v-card-title>
      <v-divider class="mx-2"/>
      <v-card-text>
        <v-form @submit.prevent="handleLogin">
          <v-text-field
                        label="ID"
                        type="text"
                        v-model="user.username"
                        v-validate="'required'"
                        :rules="usernameRules"
                        required
                        name="username"/>
          <v-text-field
                        label="PW"
                        type="password"
                        v-model="user.password"
                        v-validate="'required'"
                        :rules="passwordRules"
                        required
                        name="password"/>
          <v-btn block color="info" type="submit" class="my-2">
              Login
          </v-btn>
          <div v-if="message" class="alert alert-danger" role="alert">{{message}}</div>
          <v-btn color="info" plain to="/register">
            新しいアカウントを作りますか?
          </v-btn>
        </v-form>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
import User from '../models/user';

export default {
  name: 'Login',
  data() {
    return {
      user: new User('', ''),
      loading: false,
      message: '',
      valid: false,
      usernameRules: [
          v => !!v || 'IDを入力してください。'
      ],
      passwordRules: [
          v => !!v || 'パスワードを入力してください。'
      ]
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  created() {
    if (this.loggedIn) {
      this.$router.push('/profile');
    }
  },
  methods: {
    handleLogin() {
      this.loading = true;
      this.$validator.validateAll().then(isValid => {
        if (!isValid) {
          this.loading = false;
          return;
        }

        if (this.user.username && this.user.password) {
          this.$store.dispatch('auth/login', this.user).then(
            () => {
              this.$router.push('/');
            },
            error => {
              this.loading = false;
              this.message =
                (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString();
            }
          );
        }
      });
    }
  }
};
</script>