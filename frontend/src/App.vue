<template>
  <v-app>
    <v-navigation-drawer app permanent dark>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="text-xl-h5">
            <div class="py-lg-3">
              受注管理システム
            </div>
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>

      <v-divider class="mx-2"/>

      <div v-if="currentUser = this.$store.state.auth.user">
        <v-menu absolute offset-y open-on-click>
          <template v-slot:activator="{ on }">
        <v-list-item two-line v-on="on">
          <v-list-item-avatar>
            <img src="./img/profile-image.jpg">
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title>{{currentUser.username}}</v-list-item-title>
            <v-list-item-subtitle>ログイン中</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-divider class="mx-2"/>
          </template>
          <v-list>
            <v-list-item @click.prevent="profile">
              <v-list-item-icon>
                <v-icon>mdi-account</v-icon>
              </v-list-item-icon>
              <v-list-item-title>プロフィール</v-list-item-title>
            </v-list-item>
            <v-list-item @click.prevent="logout">
              <v-list-item-icon>
                <v-icon>mdi-logout-variant</v-icon>
              </v-list-item-icon>
              <v-list-item-title>ログアウト</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>

      <v-list dense nav>
        <v-list-item
            v-for="item in items"
            :key="item.title"
            link
            :to="item.to"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title class="text-xl-subtitle-1">{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-divider/>

        <v-list-item
            v-for="item in items2"
            :key="item.title"
            link
            :to="item.to"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title class="text-xl-subtitle-1">{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

      <v-divider/>

      <v-list-item
          v-for="item in items3"
          :key="item.title"
          link
          :to="item.to"
      >
        <v-list-item-icon>
          <v-icon>{{ item.icon }}</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title class="text-xl-subtitle-1">{{ item.title }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main class="overflow-hidden">
      <router-view/>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: 'App',
  data: () => ({
    items: [
      { title: 'ホーム', icon: 'mdi-home', to: '/'},
      { title: '見積書', icon: 'mdi-clipboard-list', to: '#1'},
      { title: '納品書', icon: 'mdi-truck-delivery', to: '#2'},
      { title: '請求書', icon: 'mdi-cash', to: '#3'},
      { title: '受注管理', icon: 'mdi-file-cog', to: '/order'},
      { title: 'レポート', icon: 'mdi-file-chart', to: '#5'},
    ],
    items2: [
      { title: '受信箱', icon: 'mdi-mailbox', to: '#6'}
    ],
    items3: [
      { title: '取引先', icon: 'mdi-folder-account', to: '/client'},
      { title: '品目管理', icon: 'mdi-database-cog', to: '#8'},
      { title: 'ご利用履歴', icon: 'mdi-history', to: '#9'},
      { title: '設定', icon: 'mdi-cog', to: '/setting'},
    ],
  }),

  methods: {
    logout() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    },
    profile() {
      this.$router.push('/profile');
    }
  },
};
</script>

<style>
html {
  overflow: auto;
}
</style>
