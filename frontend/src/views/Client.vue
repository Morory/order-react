<template>
  <div class="mt-10">
    <v-layout wrap>
    <v-flex sm12 md8 offset-md2>
      <v-layout align-center justify-space-between>
        <span class="text-h4">
          取引先
        </span>
      </v-layout>
      <v-divider class="mt-7 mb-3"/>
      <v-data-table
        v-model="selected"
        show-select
        item-key="id"
        :headers="headers"
        :items="clients"
        :search="search"
        class="elevation-1"
        :footer-props="{
          showFirstLastPage: true,
          'items-per-page-text': '表示数'
        }"
      >
        <template v-slot:header.bookmarked="{ header }">
          <v-icon>mdi-star</v-icon>
        </template>
      <template v-slot:top>
        <v-toolbar
            flat
        >
          <v-col cols="12" sm="6" md="5">
          <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="取引先名、管理コード、電話番号など"
              single-line
              hide-details
          ></v-text-field>
          </v-col>
          <v-spacer></v-spacer>
          <v-dialog
              v-model="dialog"
              max-width="500px"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  color="info"
                  dark
                  class="text-xl-body-1 font-weight-bold mb-2 px-10"
                  v-bind="attrs"
                  v-on="on"
              >
                <span>取引先の新規登録</span>
              </v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="text-h5">{{ formTitle }}</span>
              </v-card-title>

              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col
                        cols="12"
                        sm="6"
                        md="7"
                    >
                      <v-text-field
                          v-model="editedItem.name"
                          label="取引先名"
                      ></v-text-field>
                    </v-col>
                    <v-col
                        cols="12"
                        sm="6"
                        md="5"
                    >
                      <v-text-field
                          v-model="editedItem.manager"
                          label="担当者名"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col
                        cols="12"
                        sm="6"
                        md="5"
                    >
                      <v-text-field
                          v-model="editedItem.tel"
                          label="電話番号"
                      ></v-text-field>
                    </v-col>
                    <v-col
                        cols="12"
                        sm="6"
                        md="7"
                    >
                      <v-text-field
                          v-model="editedItem.address"
                          label="送り先"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                    color="blue darken-1"
                    text
                    @click="close"
                >
                  キャンセル
                </v-btn>
                <v-btn
                    color="blue darken-1"
                    text
                    @click="save"
                >
                  完了
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-dialog v-model="dialogDelete" max-width="500px">
            <v-card>
              <v-card-title class="text-center justify-center">本当にこの取引先を削除しますか?</v-card-title>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDelete">キャンセル</v-btn>
                <v-btn color="blue darken-1" text @click="deleteItemConfirm">削除</v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
        <template v-slot:item.bookmarked="{ item }">
          <v-icon @click="changeBookmark(item)">{{ item.bookmarked ? 'mdi-star' : 'mdi-star-outline'}}</v-icon>
        </template>
      <template v-slot:item.actions="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="editItem(item)"
        >
          mdi-pencil
        </v-icon>
        <v-icon
            small
            @click="deleteItem(item)"
        >
          mdi-delete
        </v-icon>
      </template>
      <template v-slot:no-data>
        <span>登録された取引先がありません。</span>
      </template>
        <template v-slot:footer.prepend>
          <download-csv
            :data = "selected"
            :name = "'取引先.csv'"
            :labels = "labels"
            :fields = "fields"
          >
          <v-btn plain color="primary">
            <v-icon>
              mdi-download
            </v-icon>
            チェックしたリストをCSVでダウンロード
          </v-btn>
          </download-csv>
        </template>
      </v-data-table>
    </v-flex>
    </v-layout>
  </div>
</template>

<script>
import ClientService from "../services/client.service";
import JsonCsv from 'vue-json-csv';
export default {
  name: "Client",
  components: {
    'downloadCsv': JsonCsv
  },
  data: () => ({
    dialog: false,
    dialogDelete: false,
    search: '',
    selected: [],
    headers: [
      { text: '取引先', align: 'start', value: 'name' },
      { text: 'bookmark', value: 'bookmarked'},
      { text: '管理コード', value: 'id' },
      { text: '担当者名', value: 'manager' },
      { text: '電話番号', value: 'tel' },
      { text: '送り先', value: 'address' },
      { text: '操作', value: 'actions', sortable: false },
    ],
    labels: {
      'id': '管理コード',
      'name': '取引先名',
      'manager': '担当者名',
      'tel': '電話番号',
      'address': '送り先'
    },
    fields: [
        'id', 'name', 'manager', 'tel', 'address'
    ],
    clients: [],
    editedIndex: -1,
    editedItem: {
      id: '',
      name: '',
      manager: '',
      tel: '',
      address: '',
      bookmarked: ''
    },
    defaultItem: {
      id: '',
      name: '',
      manager: '',
      tel: '',
      address: '',
      bookmarked: ''
    },
  }),

  computed: {
    formTitle () {
      return this.editedIndex === -1 ? '新規登録' : '修整'
    },
  },

  watch: {
    dialog (val) {
      val || this.close()
    },
    dialogDelete (val) {
      val || this.closeDelete()
    },
  },

  created () {
    this.retrieveClients()
  },

  methods: {
    changeBookmark (item) {
      this.editedIndex = this.clients.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.editedItem.bookmarked = !item.bookmarked;
      this.save();
    },
    retrieveClients () {
      ClientService.getAll()
        .then(response => {
          if(response.status === 200) {
            this.clients = response.data;
          }
        });
    },

    editItem (item) {
      this.editedIndex = this.clients.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    deleteItem (item) {
      this.editedIndex = this.clients.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    deleteItemConfirm () {
      ClientService.delete(this.editedItem.id)
        .then(response => {
          if(response.status === 200) {
            this.clients.splice(this.editedIndex, 1)
            this.closeDelete()
          }
        })
    },

    close () {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    closeDelete () {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    save () {
      if (this.editedIndex > -1) {
        ClientService.update(this.editedItem);
        Object.assign(this.clients[this.editedIndex], this.editedItem)
      } else {
        ClientService.create(this.editedItem)
            .then(response => {
              this.clients.push(response.data);
            })
      }
      this.close()
    },
  },
}
</script>

<style scoped>

</style>