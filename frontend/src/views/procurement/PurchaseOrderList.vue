<template>
  <div class="app-container">
    <el-card class="filter-container">
      <div class="filter-form">
        <el-form :inline="true" :model="searchForm" size="small">
          <el-form-item label="Order Number">
            <el-input v-model="searchForm.orderNumber" placeholder="Enter order number" clearable style="width: 200px;" @keyup.enter.native="handleSearch" />
          </el-form-item>
          <el-form-item label="Supplier">
            <el-select v-model="searchForm.supplierId" placeholder="Select supplier" clearable style="width: 180px;">
              <el-option
                v-for="supplier in supplierOptions"
                :key="supplier.supplierId"
                :label="supplier.supplierName"
                :value="supplier.supplierId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="Order Status">
            <el-select v-model="searchForm.status" placeholder="Select status" clearable style="width: 120px;">
              <el-option label="Pending Confirmation" :value="1" />
              <el-option label="Confirmed" :value="2" />
              <el-option label="Received" :value="3" />
              <el-option label="Canceled" :value="4" />
            </el-select>
          </el-form-item>
          <el-form-item label="Order Date">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="to"
              start-placeholder="Start date"
              end-placeholder="End date"
              value-format="yyyy-MM-dd"
              style="width: 350px;"
              @change="handleDateChange"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">Search</el-button>
            <el-button icon="el-icon-refresh" @click="resetSearch">Reset</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-card class="table-container">
      <div class="operation-container">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">Create Purchase Order</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="purchaseOrderList"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column prop="orderNumber" label="Order Number" width="160" />
        <el-table-column prop="supplierName" label="Supplier" min-width="180" show-overflow-tooltip />
        <el-table-column prop="orderDate" label="Order Date" width="120" />
        <el-table-column prop="expectedDate" label="Expected Arrival" width="120" />
        <el-table-column prop="totalAmount" label="Order Amount" width="120" align="right">
          <template slot-scope="scope">
            ¥{{ scope.row.totalAmount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Status" width="90">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operator" label="Operator" width="100" />
        <el-table-column label="Actions" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">View</el-button>
            <el-button
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
              :disabled="scope.row.status === 3 || scope.row.status === 4"
            >
              Edit
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleConfirm(scope.row)"
              v-if="scope.row.status === 1"
            >
              Confirm
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleComplete(scope.row)"
              v-if="scope.row.status === 2"
            >
              Mark Received
            </el-button>
            <el-button
              type="text"
              size="small"
              style="color: #F56C6C;"
              @click="handleCancel(scope.row)"
              :disabled="scope.row.status === 3 || scope.row.status === 4"
            >
              Cancel
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- purchase order form dialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" @close="handleDialogClose">
      <el-form ref="orderForm" :model="orderForm" :rules="orderRules" label-width="120px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Supplier" prop="supplierId">
              <el-select v-model="orderForm.supplierId" placeholder="Select supplier" style="width: 100%;">
                <el-option
                  v-for="supplier in supplierOptions"
                  :key="supplier.supplierId"
                  :label="supplier.supplierName"
                  :value="supplier.supplierId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Expected Arrival" prop="expectedDate">
              <el-date-picker
                v-model="orderForm.expectedDate"
                type="date"
                placeholder="Select expected arrival date"
                value-format="yyyy-MM-dd"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="Remarks">
          <el-input v-model="orderForm.remarks" type="textarea" :rows="2" placeholder="Enter remarks" />
        </el-form-item>

        <el-divider content-position="left">Product Details</el-divider>

        <div class="items-container">
          <div v-for="(item, index) in orderForm.items" :key="index" class="item-row">
            <el-row :gutter="10">
              <el-col :span="8">
                <el-input v-model="item.productName" placeholder="Product name" />
              </el-col>
              <el-col :span="4">
                <el-input v-model="item.quantity" placeholder="Quantity" type="number" @change="calculateSubtotal(index)" />
              </el-col>
              <el-col :span="4">
                <el-select v-model="item.unit" placeholder="Unit">
                  <el-option label="kg" value="kg" />
                  <el-option label="Piece" value="个" />
                  <el-option label="Case" value="箱" />
                  <el-option label="Bag" value="袋" />
                </el-select>
              </el-col>
              <el-col :span="4">
                <el-input v-model="item.unitPrice" placeholder="Unit price" type="number" @change="calculateSubtotal(index)" />
              </el-col>
              <el-col :span="3">
                <el-input :value="item.subtotal.toFixed(2)" placeholder="Subtotal" disabled />
              </el-col>
              <el-col :span="1">
                <el-button type="text" icon="el-icon-delete" style="color: #F56C6C;" @click="removeItem(index)" />
              </el-col>
            </el-row>
          </div>

          <el-button type="text" icon="el-icon-plus" @click="addItem" style="margin-top: 10px;">Add Product</el-button>
        </div>

        <el-divider />

        <el-row type="flex" justify="end">
          <el-col :span="8">
            <div class="total-section">
              <div class="total-row">
                <span>Total amount:</span>
                <span class="total-amount">¥{{ totalAmount.toFixed(2) }}</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">Confirm</el-button>
      </div>
    </el-dialog>

    <!-- order details dialog -->
    <el-dialog title="Purchase Order Details" :visible.sync="detailVisible" width="800px">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="Order Number">{{ currentOrder.orderNumber }}</el-descriptions-item>
        <el-descriptions-item label="Supplier">{{ currentOrder.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="Order Date">{{ currentOrder.orderDate }}</el-descriptions-item>
        <el-descriptions-item label="Expected Arrival">{{ currentOrder.expectedDate }}</el-descriptions-item>
        <el-descriptions-item label="Order Amount" :span="2">
          <span style="font-weight: bold; color: #E6A23C;">¥{{ currentOrder.totalAmount ? currentOrder.totalAmount.toFixed(2) : '0.00' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="Status">
          <el-tag :type="getStatusType(currentOrder.status)">
            {{ getStatusText(currentOrder.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Operator">{{ currentOrder.operator }}</el-descriptions-item>
        <el-descriptions-item label="Remarks" :span="2">{{ currentOrder.remarks || 'None' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">Product Details</el-divider>

      <el-table :data="currentOrder.items" border size="small" style="width: 100%;">
        <el-table-column prop="productName" label="Product" />
        <el-table-column prop="quantity" label="Qty" width="80" />
        <el-table-column prop="unit" label="Unit" width="60" />
        <el-table-column prop="unitPrice" label="Unit Price" width="100" align="right">
          <template slot-scope="scope">
            ¥{{ scope.row.unitPrice.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="subtotal" label="Subtotal" width="100" align="right">
          <template slot-scope="scope">
            ¥{{ scope.row.subtotal.toFixed(2) }}
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">Close</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getPurchaseOrderList,
  getPurchaseOrder,
  addPurchaseOrder,
  updatePurchaseOrder,
  deletePurchaseOrder,
  updatePurchaseOrderStatus,
  getSupplierList
} from '@/api/procurement'

export default {
  name: 'PurchaseOrderList',
  data() {
    return {
      loading: false,
      submitting: false,
      purchaseOrderList: [],
      supplierOptions: [],
      searchForm: {
        orderNumber: '',
        supplierId: '',
        status: null,
        startDate: '',
        endDate: ''
      },
      dateRange: [],
      dialogVisible: false,
      detailVisible: false,
      dialogTitle: '',
      isEdit: false,
      currentOrder: {},
      orderForm: {
        orderId: '',
        supplierId: '',
        expectedDate: '',
        remarks: '',
        items: []
      },
      orderRules: {
        supplierId: [
          { required: true, message: 'Please select a supplier', trigger: 'change' }
        ],
        expectedDate: [
          { required: true, message: 'Please select the expected arrival date', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    totalAmount() {
      return this.orderForm.items.reduce((total, item) => {
        return total + (parseFloat(item.subtotal) || 0)
      }, 0)
    }
  },
  created() {
    this.fetchSupplierOptions()
    this.fetchData()
  },
  methods: {
    async fetchSupplierOptions() {
      try {
        const response = await getSupplierList({})
        if (response.status === 200) {
          this.supplierOptions = response.data
        }
      } catch (error) {
        console.error('Failed to fetch supplier list:', error)
      }
    },

    async fetchData() {
      this.loading = true
      try {
        const response = await getPurchaseOrderList(this.searchForm)
        
        /* mock 数据备用
        const mockData = [
          { orderId: 'PO20231201001', orderNumber: 'PO-20231201-001', supplierName: '北京农产品批发公司', orderDate: '2023-12-01', totalAmount: 15800.00, status: 2, operator: '采购员001' },
          { orderId: 'PO20231201002', orderNumber: 'PO-20231201-002', supplierName: '上海食品配送中心', orderDate: '2023-12-01', totalAmount: 23500.00, status: 1, operator: '采购员002' }
        ]
        */
        
        if (response.status === 200) {
          this.purchaseOrderList = response.data
        } else {
          this.$message.error(response.msg || 'Failed to fetch purchase order list')
        }
      } catch (error) {
        console.error('Failed to fetch purchase order list:', error)
        this.$message.error('Failed to fetch purchase order list')
      } finally {
        this.loading = false
      }
    },

    handleDateChange(dates) {
      if (dates && dates.length === 2) {
        this.searchForm.startDate = dates[0]
        this.searchForm.endDate = dates[1]
      } else {
        this.searchForm.startDate = ''
        this.searchForm.endDate = ''
      }
    },

    handleSearch() {
      this.fetchData()
    },

    resetSearch() {
      this.searchForm = {
        orderNumber: '',
        supplierId: '',
        status: null,
        startDate: '',
        endDate: ''
      }
      this.dateRange = []
      this.fetchData()
    },

    handleAdd() {
      this.dialogTitle = 'Create Purchase Order'
      this.isEdit = false
      this.orderForm = {
        orderId: '',
        supplierId: '',
        expectedDate: '',
        remarks: '',
        items: []
      }
      this.addItem() // add an empty row by default
      this.dialogVisible = true
    },

    async handleView(row) {
      this.currentOrder = { ...row }
      this.detailVisible = true
    },

    async handleEdit(row) {
      this.dialogTitle = 'Edit Purchase Order'
      this.isEdit = true

      try {
        const response = await getPurchaseOrder(row.orderId)
        if (response.status === 200) {
          this.orderForm = { ...response.data }
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || 'Failed to fetch order details')
        }
      } catch (error) {
        console.error('Failed to fetch order details:', error)
        this.$message.error('Failed to fetch order details')
        // if fetch fails, just use the row data for editing
        this.orderForm = { ...row }
        this.dialogVisible = true
      }
    },

    async handleConfirm(row) {
      try {
        await this.$confirm('Are you sure you want to confirm this purchase order?', 'Reminder', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        })

        const response = await updatePurchaseOrderStatus(row.orderId, 2)
        if (response.status === 200) {
          this.$message.success('Order confirmed successfully')
          this.fetchData()
        } else {
          this.$message.error(response.msg || 'Confirmation failed')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Failed to confirm order:', error)
          this.$message.error('Confirmation failed')
        }
      }
    },

    async handleComplete(row) {
      try {
        await this.$confirm('Confirm this order has been received?', 'Reminder', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        })

        const response = await updatePurchaseOrderStatus(row.orderId, 3)
        if (response.status === 200) {
          this.$message.success('Arrival confirmed successfully')
          this.fetchData()
        } else {
          this.$message.error(response.msg || 'Operation failed')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Failed to confirm arrival:', error)
          this.$message.error('Operation failed')
        }
      }
    },

    async handleCancel(row) {
      try {
        await this.$confirm('Are you sure you want to cancel this purchase order?', 'Reminder', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        })

        const response = await updatePurchaseOrderStatus(row.orderId, 4)
        if (response.status === 200) {
          this.$message.success('Canceled successfully')
          this.fetchData()
        } else {
          this.$message.error(response.msg || 'Cancellation failed')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Failed to cancel order:', error)
          this.$message.error('Cancellation failed')
        }
      }
    },

    addItem() {
      this.orderForm.items.push({
        productName: '',
        quantity: 1,
        unit: 'kg',
        unitPrice: 0,
        subtotal: 0
      })
    },

    removeItem(index) {
      if (this.orderForm.items.length > 1) {
        this.orderForm.items.splice(index, 1)
      } else {
        this.$message.warning('Keep at least one product')
      }
    },

    calculateSubtotal(index) {
      const item = this.orderForm.items[index]
      const quantity = parseFloat(item.quantity) || 0
      const unitPrice = parseFloat(item.unitPrice) || 0
      item.subtotal = quantity * unitPrice
    },

    async handleSubmit() {
      this.$refs.orderForm.validate(async (valid) => {
        if (!valid) return

        // check product details
        const hasValidItems = this.orderForm.items.some(item => {
          return item.productName && item.quantity > 0 && item.unitPrice > 0
        })

        if (!hasValidItems) {
          this.$message.warning('Please add at least one valid product')
          return
        }

        this.submitting = true
        try {
          // calculate total amount
          const orderData = {
            ...this.orderForm,
            totalAmount: this.totalAmount
          }

          let response
          if (this.isEdit) {
            response = await updatePurchaseOrder(orderData)
          } else {
            response = await addPurchaseOrder(orderData)
          }

          if (response.status === 200) {
            this.$message.success(response.msg || (this.isEdit ? 'Updated successfully' : 'Created successfully'))
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(response.msg || (this.isEdit ? 'Update failed' : 'Creation failed'))
          }
        } catch (error) {
          console.error('Submit failed:', error)
          this.$message.error('Operation failed')
        } finally {
          this.submitting = false
        }
      })
    },

    handleDialogClose() {
      this.$refs.orderForm && this.$refs.orderForm.resetFields()
    },

    getStatusType(status) {
      const statusMap = {
        1: 'warning',  // pending confirmation
        2: 'primary',  // confirmed
        3: 'success',  // received
        4: 'info'      // canceled
      }
      return statusMap[status] || 'info'
    },

    getStatusText(status) {
      const statusMap = {
        1: 'Pending Confirmation',
        2: 'Confirmed',
        3: 'Received',
        4: 'Canceled'
      }
      return statusMap[status] || 'Unknown'
    }
  }
}
</script>

<style scoped>
.filter-container {
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
}

.operation-container {
  margin-bottom: 20px;
}

.items-container {
  max-height: 300px;
  overflow-y: auto;
}

.item-row {
  margin-bottom: 10px;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.total-section {
  text-align: right;
  padding: 10px;
}

.total-row {
  font-size: 16px;
  font-weight: bold;
}

.total-amount {
  color: #E6A23C;
  font-size: 18px;
}
</style>