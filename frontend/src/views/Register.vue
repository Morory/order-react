<template>
  <div>
    <v-card elevation="4" class="mx-auto mt-16" max-width="400">
      <v-card-title>
        <v-layout align-center justify-space-between>
        <h3 class="headline">
          Register
        </h3>
        <v-icon color="info" @click.prevent="$router.go(-1)">
          mdi-keyboard-backspace
        </v-icon>
        </v-layout>
      </v-card-title>
      <v-divider class="mx-2"/>
      <v-card-text>
        <v-form @submit.prevent="handleRegister">
          <div v-if="!successful">
            <v-text-field
                          label="ID"
                          type="text"
                          v-model="user.username"
                          v-validate="'required|min:3|max:20'"
                          :rules="usernameRules"
                          required
                          name="username"/>
            <v-text-field
                          label="EMAIL"
                          type="email"
                          v-model="user.email"
                          v-validate="'required|max:50'"
                          :rules="emailRules"
                          required
                          name="username"/>
            <v-text-field outline
                          label="PW"
                          type="password"
                          v-model="user.password"
                          v-validate="'required|min:6|max:40'"
                          :rules="passwordRules"
                          required
                          name="password"/>
            <v-btn block color="info" type="submit" class="my-2">
              Register
            </v-btn>
          </div>
        </v-form>
        <div
            v-if="message"
            class="text-h6"
            style="text-align: center"
            :class="successful ? 'alert-success' : 'alert-danger'"
        >{{message}}
          <div v-if="successful">
            <v-btn color="info" plain to="/login" class="text-h6">
              ログインしますか？
            </v-btn>
          </div>
        </div>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
import User from '../models/user';

export default {
  name: 'Register',
  data() {
    return {
      user: new User('', '', ''),
      submitted: false,
      successful: false,
      message: '',
      valid: false,
      usernameRules: [
        v => !!v || 'IDを入力してください。',
        v => (v && v.length >= 3) || 'IDには3文字以上入力してください。'
      ],
      emailRules: [
        v => !!v || 'メールアドレスを入力してください。',
        v => /.+@.+\..+/.test(v) || '正しいメールアドレス形式を入力してください。',
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
  mounted() {
    if (this.loggedIn) {
      this.$router.push('/profile');
    }
  },
  methods: {
    handleRegister() {
      this.message = '';
      this.submitted = true;
      this.$validator.validate().then(isValid => {
        if (isValid) {
          this.$store.dispatch('auth/register', this.user).then(
            data => {
              this.message = data.message;
              this.successful = true;
            },
            error => {
              this.message =
                (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString();
              this.successful = false;
            }
          );
        }
      });
    }
  }
};
</script>