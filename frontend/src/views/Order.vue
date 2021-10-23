<template>
  <div class="mt-10">
    <v-layout wrap>
      <v-flex sm12 md8 offset-md2>
        <v-layout align-center justify-space-between>
          <span class="text-h4">
            受注管理
          </span>
        </v-layout>
        <v-divider class="mt-7 mb-3"/>
        <v-card>
          <v-tabs
              icons-and-text
              v-model="tab"
          >
            <v-tab
                v-for="item in items"
                :key="item.text"
                @click.prevent="clearSearch(tab)"
            >
              {{item.text}}
              <v-icon>{{item.icon}}</v-icon>
            </v-tab>
            <v-spacer/>
            <v-text-field
                v-model="search"
                prepend-inner-icon="mdi-magnify"
                label="取引先名"
                outlined
                dense
                @input="searchOrder(tab)"
                class="mt-3 mr-10"
            >
            </v-text-field>
            <v-dialog
                v-model="dialog"
                persistent
                max-width="800px"
            >
              <template v-slot:activator="{ on, attrs }">
              <v-btn
                  v-bind="attrs"
                  @click.prevent="prepareOrderForm"
                  v-on="on"
                  color="info"
                  class="text-xl-body-1 font-weight-bold mt-3 mr-3 px-10"
              >
                受注情報の作成
              </v-btn>
              </template>
              <v-card>
                <v-form
                    @submit.prevent="save"
                >
                  <div id="pdfCard">
                <v-card-title>
                  <span class="text-h5">受注情報</span>
                  <v-spacer></v-spacer>
                  <v-btn
                      plain
                      small
                      @click="clear"
                  >
                    <v-icon>mdi-close</v-icon>
                  </v-btn>
                </v-card-title>
                <v-divider class="mx-5"></v-divider>
                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col
                          cols="12"
                          sm="6"
                          md="6"
                      >
                        <v-select
                            label="取引先名"
                            v-model="editedOrder.clientId"
                            :items="clientList"
                            item-text="name"
                            item-value="id"
                            :rules="clientRules"
                            >
                          <template v-slot:no-data>
                            先に取引先を登録してください。
                          </template>
                        </v-select>
                      </v-col>
                      <v-col
                          cols="12"
                          sm="3"
                          md="3"
                      >
                        <v-menu
                            v-model="menu1"
                            :close-on-content-click="true"
                            transition="scale-transition"
                            offset-y
                            min-width="auto"
                        >
                          <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                                v-model="editedOrder.orderDate"
                                label="注文日"
                                prepend-inner-icon="mdi-calendar"
                                readonly
                                v-bind="attrs"
                                v-on="on"
                            ></v-text-field>
                          </template>
                          <v-date-picker
                              locale="ja-JP"
                              no-title
                              scrollable
                              v-model="editedOrder.orderDate"
                              @input="menu1 = false"
                          >
                          </v-date-picker>
                        </v-menu>
                      </v-col>
                      <v-col
                          cols="12"
                          sm="3"
                          md="3"
                      >
                        <v-menu
                            ref="menu"
                            v-model="menu2"
                            :close-on-content-click="true"
                            transition="scale-transition"
                            offset-y
                            min-width="auto"
                        >
                          <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                                v-model="editedOrder.deliveryDate"
                                label="納期日"
                                prepend-inner-icon="mdi-calendar"
                                readonly
                                clearable
                                @click:clear="editedOrder.deliveryDate = null"
                                placeholder="指定なし"
                                v-bind="attrs"
                                v-on="on"
                            ></v-text-field>
                          </template>
                          <v-date-picker
                              locale="ja-JP"
                              no-title
                              scrollable
                              v-model="editedOrder.deliveryDate"
                              @input="menu2 = false"
                          >
                          </v-date-picker>
                        </v-menu>
                      </v-col>
                    </v-row>
                    <v-row v-if="editedOrder.client">
                      <v-col
                          cols="12"
                          sm="4"
                      >
                        <v-text-field
                            v-model="editedOrder.client.manager"
                            label="担当者名"
                            readonly
                        >
                        </v-text-field>
                      </v-col>
                      <v-col
                          cols="12"
                          sm="4"
                      >
                        <v-text-field
                            v-model="editedOrder.client.tel"
                            label="電話番号"
                            readonly
                        >
                        </v-text-field>
                      </v-col>
                      <v-col
                          cols="12"
                          sm="4"
                      >
                        <v-text-field
                            v-model="editedOrder.client.address"
                            label="送り先"
                            readonly
                        >
                        </v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col
                          cols="12"
                          sm="4"
                      >
                        <v-text-field
                            v-model="editedOrder.orderNumber"
                            label="注文番号"
                            readonly
                        >
                        </v-text-field>
                      </v-col>
                      <v-col
                          cols="12"
                          sm="4"
                      >
                        <v-text-field
                            label="件名"
                            v-model="editedOrder.title"
                        >
                        </v-text-field>
                      </v-col>
                      <v-col
                          cols="12"
                          sm="4"
                      >
                        <v-select
                            v-model="editedOrder.completed"
                            label="ステータス"
                            :items="statuses"
                            item-text="text"
                            item-value="value"
                            return-object
                        >
                        </v-select>
                      </v-col>
                    </v-row>
                    <v-simple-table
                        style="overflow-x: hidden"
                    >
                      <template v-slot:default>
                        <thead>
                          <tr>
                            <th class="text-center" style="width: 30%">
                              品番、品名
                            </th>
                            <th class="text-center" style="width: 13%">
                              数量
                            </th>
                            <th class="text-center" style="width: 10%">
                              単位
                            </th>
                            <th class="text-center" style="width: 15%">
                              単価
                            </th>
                            <th class="text-center" style="width: 13%">
                              税区分
                            </th>
                            <th class="text-center" style="width: 18%">
                              金額
                            </th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr v-for="(orderDetail, index) in editedOrder.orderDetails" :key="index">
                            <td>
                              <v-text-field v-model="orderDetail.name"></v-text-field>
                            </td>
                            <td>
                              <v-text-field
                                  v-model="orderDetail.amount"
                                  type="number"
                                  @change="compute(index)"
                                  min="0"
                              ></v-text-field>
                            </td>
                            <td>
                              <v-text-field v-model="orderDetail.unit"></v-text-field>
                            </td>
                            <td>
                              <v-text-field
                                  v-model="orderDetail.price"
                                  type="number"
                                  @change="compute(index)"
                                  min="0"
                              ></v-text-field>
                            </td>
                            <td>
                              <v-select
                                  :items="taxRates"
                                  v-model="orderDetail.taxRate"
                                  @change="compute(index)"
                              >
                              </v-select>
                            </td>
                            <td>
                              <v-text-field
                                  v-model="orderDetail.rowPrice"
                                  readonly
                                  :append-outer-icon="'mdi-close-circle-outline'"
                                  @click:append-outer="removeRow(index)"
                              ></v-text-field>
                            </td>
                          </tr>
                        </tbody>
                        <v-btn
                            text
                            color="primary"
                            @click.prevent="addRow">
                          <span>行の追加 +</span>
                        </v-btn>
                      </template>
                    </v-simple-table>
                  </v-container>
                  <v-divider/>
                  <v-container
                  >
                    <div class="text-right">
                      <span>小計 <span class="text-h5">{{editedOrder.subTotalPrice}}</span>円</span>
                      <v-spacer/>
                      <span>消費税 <span class="text-h5">{{editedOrder.taxPrice}}</span>円</span>
                      <v-spacer/>
                      <span>合計 <span class="text-h5">{{editedOrder.totalPrice}}</span>円</span>
                    </div>
                  </v-container>
                </v-card-text>
                  </div>
                <v-card-actions>
                  <v-btn
                      text
                      color="info"
                      @click.prevent="makePDF"
                  >
                    <v-icon>
                      mdi-download
                    </v-icon>
                    PDF
                  </v-btn>
                  <v-spacer></v-spacer>
                  <v-btn
                    color="blue darken-1"
                    class="text-h6"
                    text
                    type="submit"
                  >
                    保存
                  </v-btn>
                </v-card-actions>
                </v-form>
              </v-card>
            </v-dialog>
          </v-tabs>
          <v-tabs-items
              v-model="tab"
          >
            <v-tab-item
                v-for="(item, tabIndex) in items"
                :key="item.text"
            >
              <v-container fluid>
                <v-data-iterator
                    :items="item.list"
                    :footer-props="{
                       'items-per-page-text': '表示数'
                    }"
                >
                  <template v-slot:no-data>
                    <div class="text-center">
                    <span class="lightgray--text text-xl-body-1">登録された受注情報がありません。</span>
                    </div>
                  </template>
                  <v-card flat>
                    <v-row>
                      <v-col class="text-right">
                    <v-btn
                        v-if="2 === tabIndex"
                        text
                        color="info"
                        @click.prevent="dialogDeleteAll = true"
                    >
                      <v-icon>
                        mdi-delete-forever
                      </v-icon>
                      リストを永久削除
                    </v-btn>
                      </v-col>
                    </v-row>
                  <v-row
                      v-for="(order) in items[tabIndex].list"
                      :key="order.id"
                      v-bind:id="order.id"
                      class="order-row"
                  >
                    <v-col>
                      <v-hover v-slot="{ hover }">
                      <v-card
                          :elevation="hover ? 5 : 1"
                      >
                        <v-layout
                            justify-space-between
                        >
                          <v-list-item two-line>
                            <v-list-item-content>
                              <v-list-item-title
                                  class="text-h5"
                                  :class="hover ? 'primary--text' : 'secondary--text'"
                              >
                                {{order.client.name}}
                              </v-list-item-title>
                              <v-list-item-subtitle>
                                {{order.orderNumber}}
                              </v-list-item-subtitle>
                            </v-list-item-content>
                          </v-list-item>
                          <v-spacer/>
                          <v-checkbox
                              v-model="checkedOrder"
                              color="info"
                              class="mb-4"
                              :value="order.id"
                              @change="checkOrder(order.id)"
                          >
                          </v-checkbox>
                          <v-hover v-if="2 === tabIndex" v-slot="{ hover }">
                            <v-icon
                                class="mr-3 mb-5"
                                :color="hover ? 'info' : 'lightgray'"
                                @click.prevent="restoreOrder(order.id)"
                            >
                              mdi-restore
                            </v-icon>
                          </v-hover>
                          <v-hover v-if="2 !== tabIndex" v-slot="{ hover }">
                            <v-icon
                              class="mr-3 mb-5"
                              :color="hover ? 'info' : 'lightgray'"
                              @click.prevent="deleteOrder(order.id)"
                            >mdi-delete
                            </v-icon>
                          </v-hover>
                        </v-layout>
                        <v-divider class="mx-3"/>
                        <v-layout justify-space-between>
                          <v-list-item two-line>
                            <v-list-item-content>
                              <v-list-item-title
                                  class="text--secondary text-h6"
                              >
                                {{order.title}}
                              </v-list-item-title>
                              <v-list-item-subtitle>
                                <v-icon small>mdi-calendar</v-icon>
                                {{order.orderDate.substring(0, 10)}}&nbsp;~
                                <span v-if="order.deliveryDate">&nbsp;{{order.deliveryDate.substring(0, 10)}}</span>
                              </v-list-item-subtitle>
                            </v-list-item-content>
                          </v-list-item>
                          <v-spacer/>
                          <v-btn
                              large
                              text
                              color="info"
                              class="text-h6 mt-2 mr-2 px-6"
                              @click.prevent="getOrder(order.id)"
                          >
                            受注情報を見る
                            <v-icon>
                              mdi-chevron-right
                            </v-icon>
                          </v-btn>
                        </v-layout>
                      </v-card>
                      </v-hover>
                    </v-col>
                  </v-row>
                  </v-card>
                  <template v-slot:footer.prepend>
                    <download-csv
                        :data="checkedOrderJson"
                        :name="'受注情報.csv'"
                        :labels="labels"
                        :fields="fields"
                    >
                    <v-btn text color="info">
                      <v-icon>
                        mdi-download
                      </v-icon>
                      チェックしたリストをCSVでダウンロード
                    </v-btn>
                    </download-csv>
                  </template>
                </v-data-iterator>
              </v-container>
            </v-tab-item>
          </v-tabs-items>
        </v-card>
        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-center justify-center">この受注情報をゴミ箱に捨てますか?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="dialogDelete = false">キャンセル</v-btn>
              <v-btn color="blue darken-1" text @click="deleteOrderConfirm">捨てる</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-dialog v-model="dialogRestore" max-width="500px">
          <v-card>
            <v-card-title class="text-center justify-center">この受注情報を復旧しますか?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="dialogRestore = false; this.editedOrder.id = ''">キャンセル</v-btn>
              <v-btn color="blue darken-1" text @click="restoreOrderConfirm">復旧する</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-dialog v-model="dialogDeleteAll" max-width="500px">
          <v-card>
            <v-card-title class="text-center justify-center">リストを本当に永久削除しますか。<br/>削除した後は復旧できません。</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="dialogDeleteAll = false; this.editedOrder.id = ''">キャンセル</v-btn>
              <v-btn color="blue darken-1" text @click="deleteAllOrderConfirm">永久削除する</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-flex>
    </v-layout>
  </div>
</template>

<script>
import OrderService from "../services/order.service";
import ClientService from "../services/client.service";
import JsonCsv from 'vue-json-csv';
import html2canvas from 'html2canvas';
import jspdf from 'jspdf';

export default {
  name: "Order",
  components: {
    'downloadCsv': JsonCsv
  },
  data: () => ({
    menu1: false,
    menu2: false,
    tab: null,
    search: '',
    checkbox: '',
    items: [
      {
        text: "未処理",
        icon: "mdi-file-alert-outline",
        list: []
      },
      {
        text: "処理済",
        icon: "mdi-file-check-outline",
        list: []
      },
      {
        text: "ごみ箱",
        icon: "mdi-delete-outline",
        list: []
      }
    ],
    dialog: false,
    dialogDelete: false,
    dialogRestore: false,
    dialogDeleteAll: false,
    show4: false,
    clientList: [],
    statuses: [
      {
        text: '未処理',
        value: false,
      },
      {
        text: '処理済',
        value: true
      }
    ],
    taxRates: ['10%', '15%'],
    valid: false,
    clientRules: [
      v => !!v || '取引先を選択してください。'
    ],
    editedOrder: {
      id: '',
      clientId: '',
      orderDate: new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 10),
      deliveryDate: '',
      orderNumber: '',
      title: '',
      completed: {
        text: '未処理',
        value: false,
      },
      orderDetails: [{
        name: "",
        amount: "",
        unit: "",
        price: "",
        taxRate: "10%",
        rowPrice: ""
      }],
      subTotalPrice: 0,
      taxPrice: 0,
      totalPrice: 0,
    },
    tabIndex: '',
    orderIndex: '',
    checkedOrder: [],
    checkedOrderJson: [],
    labels: {
      'orderNumber': '注文番号',
      'title': '件名',
      'client': '取引先名',
      'orderDate': '注文日',
      'deliveryDate': '納期日',
      'orderDetails': '注文詳細情報',
    },
    fields: [
      'orderNumber', 'title', 'client', 'orderDate', 'deliveryDate', 'orderDetails'
    ],
    editingStatus: '',
  }),
  methods: {
    makePDF() {
      window.html2canvas = html2canvas;
      let doc = new jspdf("p", "mm", "a4");
      let element = document.querySelector('#pdfCard');
      let canvas = doc.canvas;
      const pageWidth = 210;
      canvas.width = pageWidth;

      let w = element.offsetWidth;
      let h = element.offsetHeight;
      let imgHeight = pageWidth * h / w;

      html2canvas(element).then(function(canvas) {
        let img = canvas.toDataURL();
        doc.addImage(img, 'png', 0, 0, pageWidth, imgHeight);
        doc.save("受注情報.pdf");
      })
    },
    zf(n, digits) {
      let zero = '';
      n = n.toString();

      if(n.length < digits) {
        for(let i = 0; i < digits - n.length; i++) {
          zero += '0';
        }
      }
      return zero + n;
    },
    timezoneChange(utcTime) {
      let time = new Date(utcTime);
      return time.getFullYear() + "-" + this.zf(time.getMonth()+1, 2) + "-" + time.getDate()
    },
    getOrder(id) {
      OrderService.getOrderWithOrderDetails(id)
        .then(response => {
          if(response.status === 200) {
            let order = response.data.order;
            let orderDetails = response.data.orderDetails;
            orderDetails.forEach(function(item) {
              item.rowPrice = 0;
            })
            this.editedOrder = {
              id: order.id,
              clientId: order.client.id,
              client: order.client,
              orderDate: this.timezoneChange(order.orderDate),
              deliveryDate: order.deliveryDate ? this.timezoneChange(order.deliveryDate) : '',
              orderNumber: order.orderNumber,
              title: order.title,
              completed: {
                text: order.completed ? '処理済' : '未処理',
                value: order.completed
              },
              orderDetails: orderDetails,
              subTotalPrice: 0,
              taxPrice: 0,
              totalPrice: 0,
            }
            this.compute('justDo');
            this.dialog = true;
            this.editingStatus = 'update';
          }
        })
    },
    checkOrder(id) {
      if(this.checkedOrder.includes(id)) {
        OrderService.getOrderWithOrderDetails(id)
            .then(response => {
              if(response.status === 200) {
                let order = response.data.order;
                let orderDetails = response.data.orderDetails;
                let str = "";
                orderDetails.forEach(function(e, idx, arr) {
                  str += "{" + e.name + "," + e.amount + "," + e.unit + "," + e.price + "," + e.taxRate + "}";
                  if(!Object.is(arr.length - 1, idx)) {
                    str += ","
                  }
                })
                order.orderDate = order.orderDate.toString().substr(0, 10);
                if(order.deliveryDate) {
                  order.deliveryDate = order.deliveryDate.toString().substr(0,10);
                }
                order.client = order.client.name;
                order.orderDetails = str;
                this.checkedOrderJson.push(order);
              }
            })
      } else {
        let index = this.checkedOrderJson.findIndex((item) => {
          return item.id === id;
        })
        this.checkedOrderJson.splice(index, 1);
      }
    },
    searchOrder(tabIndex) {
      let search = this.search;
      let newArray = this.items[tabIndex].list.filter(function(e) {
        return e.client.name.includes(search);
      })
      this.items[tabIndex].list.forEach(e => {
        document.getElementById(e.id).style.display = "none";
      })
      newArray.forEach(e => {
        document.getElementById(e.id).style.display = "";
      })
    },
    clearSearch() {
      this.search = '';
      let target = document.getElementsByClassName('order-row');
      for(let i = 0; i < target.length; i++) {
        target[i].style.display = "";
      }
    },
    setClientList() {
      ClientService.getAll()
        .then(response => {
          if(response.status === 200) {
            this.clientList = response.data;
          }
        })
    },
    setOrderList() {
      this.items.forEach(e => {
        e.list = [];
      })
      OrderService.getAll()
        .then(response => {
          if(response.status === 200) {
            response.data.forEach(e => {
              e.orderDate = this.timezoneChange(e.orderDate);
              if(e.deliveryDate) {
                e.deliveryDate = this.timezoneChange(e.deliveryDate);
              }
              if(e.deleted === true) {
                this.items[2].list.push(e);
              } else if(e.completed === true) {
                this.items[1].list.push(e);
              } else {
                this.items[0].list.push(e);
              }
              }
            );
          }
        });
    },
    setOrderNumber() {
      OrderService.getOrderNumber()
        .then(response => {
          if(response.status === 200) {
            this.editedOrder.orderNumber = response.data;
          }
        });
    },
    prepareOrderForm() {
      this.editingStatus = 'create';
      this.clear();
      this.setOrderNumber();
    },
    deleteOrder(id) {
      this.editedOrder.id = id;
      this.dialogDelete = true;
    },
    restoreOrder(id) {
      this.editedOrder.id = id;
      this.dialogRestore = true;
    },
    deleteOrderConfirm() {
      OrderService.updateStatus(this.editedOrder.id)
        .then(response => {
          if(response.status === 200) {
            this.dialogDelete = false;
            this.setOrderList();
          }
        })
    },
    restoreOrderConfirm() {
      OrderService.updateStatus(this.editedOrder.id)
          .then(response => {
            if(response.status === 200) {
              this.dialogRestore = false;
              this.setOrderList();
            }
          })
    },
    deleteAllOrderConfirm() {
      OrderService.deleteAll()
        .then(response => {
          if(response.status === 200) {
            this.dialogDeleteAll = false;
            this.setOrderList();
          }
        })
    },
    addRow() {
      this.editedOrder.orderDetails.push({
        name: "",
        amount: "",
        unit: "",
        price: "",
        taxRate: "10%",
        rowPrice: ""
      });
    },
    removeRow(index) {
      this.editedOrder.orderDetails.splice(index, 1);
      this.compute('justDo');
    },
    save() {
      if(!this.editedOrder.clientId) {
        alert("取引先を選択してください");
        return;
      }
      this.editedOrder.orderDetails.forEach(e => delete e.rowPrice);
      this.editedOrder.completed = this.editedOrder.completed.value;
      this.editedOrder.list = JSON.stringify(this.editedOrder.orderDetails);
      delete this.editedOrder.orderDetails;

      if(this.editingStatus === 'create') {
      OrderService.create(this.editedOrder)
          .then(response => {
            if(response.status === 201 || response.status === 200) {
              this.setOrderList();
              this.clear();
            } else {
              alert("エラーが発生しました");
            }
          });
      }

      if(this.editingStatus === 'update') {
        OrderService.update(this.editedOrder)
            .then(response => {
              if(response.status === 200) {
                this.setOrderList();
                this.clear();
              } else {
                alert("エラーが発生しました");
              }
            })
      }
    },
    clear() {
      this.dialog = false;
      this.editedOrder = {
        id: '',
        clientId: '',
        orderDate: new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 10),
        deliveryDate: '',
        orderNumber: '',
        title: '',
        completed: {
          text: '未処理',
          value: false,
        },
        orderDetails: [{
          name: "",
          amount: "",
          unit: "",
          price: "",
          taxRate: "10%",
          rowPrice: ""
        }],
        subTotalPrice : 0,
        taxPrice : 0,
        totalPrice : 0,
      }
    },
    compute(index) {
      let editedOrder = this.editedOrder;
      let orderDetails = this.editedOrder.orderDetails;
      let orderDetail = this.editedOrder.orderDetails[index];
      if(index === 'justDo' || ( orderDetail.price && orderDetail.amount )) {
        // 계산해야할 조건이 충족되면 계산하기 전에 초기화
        editedOrder.subTotalPrice = Number(0);
        editedOrder.taxPrice = Number(0);
        editedOrder.totalPrice = Number(0);
        // 계산 시작
        for(let i = 0; i < orderDetails.length; i++) {
          if(orderDetails[i].price && orderDetails[i].amount) {
            orderDetails[i].rowPrice = Number(orderDetails[i].amount * orderDetails[i].price);
            editedOrder.subTotalPrice += orderDetails[i].rowPrice;
            editedOrder.taxPrice += Math.floor( orderDetails[i].price * ( orderDetails[i].taxRate.replace("%", "") / 100 ) ) * orderDetails[i].amount;
            editedOrder.totalPrice = editedOrder.subTotalPrice + editedOrder.taxPrice;
          }
        }
      }
    }
  },
  created() {
    this.setOrderList();
    this.setClientList();
  }
}
</script>

<style scoped>

</style>