<template>
  <div class="mt-10">
    <v-layout wrap>
      <v-flex sm12 md8 offset-md2>
        <v-layout align-center justify-space-between>
          <span class="text-h4">
            設定
          </span>
        </v-layout>
        <v-divider class="mt-7 mb-3"/>
        <v-switch
            v-model="$vuetify.theme.dark"
            inset
            label="ダーク·モード"
            persistent-hint
        ></v-switch>
      </v-flex>
    </v-layout>
  </div>
</template>

<script>
export default {
  name: "Setting"
}
</script>

<style scoped>

</style>