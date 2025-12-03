<template>
  <div class="app-container">
    <!-- search section -->
    <el-card class="filter-container">
      <div class="filter-form">
        <el-form :inline="true" :model="searchForm" size="small">
          <el-form-item label="Promotion Name">
            <el-input v-model="searchForm.promotionName" placeholder="Enter promotion name" clearable style="width: 200px;" @keyup.enter.native="handleSearch" />
          </el-form-item>
          <el-form-item label="Promotion Type">
            <el-select v-model="searchForm.promotionType" placeholder="Select" clearable style="width: 150px;">
              <el-option label="Percentage" :value="1" />
              <el-option label="Cash Discount" :value="2" />
              <el-option label="Buy & Get" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="Status">
            <el-select v-model="searchForm.status" placeholder="Select" clearable style="width: 120px;">
              <el-option label="Not Started" :value="0" />
              <el-option label="Active" :value="1" />
              <el-option label="Ended" :value="2" />
              <el-option label="Canceled" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">Search</el-button>
            <el-button icon="el-icon-refresh" @click="resetSearch">Reset</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- table section -->
    <el-card class="table-container">
      <div class="operation-container">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">Add Promotion</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="promotionList"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column prop="promotionId" label="Promotion ID" width="100" />
        <el-table-column prop="promotionName" label="Promotion Name" min-width="150" show-overflow-tooltip />
        <el-table-column prop="promotionType" label="Promotion Type" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPromotionTypeTag(scope.row.promotionType)">
              {{ getPromotionTypeName(scope.row.promotionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Benefit" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.promotionType === 1">{{ formatDiscountRate(scope.row.discountRate) }}</span>
            <span v-else-if="scope.row.promotionType === 2">Spend & save ¥{{ scope.row.discountAmount }}</span>
            <span v-else>Buy & get</span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="Start Time" width="160" />
        <el-table-column prop="endTime" label="End Time" width="160" />
        <el-table-column prop="memberOnly" label="Members Only" width="110">
          <template slot-scope="scope">
            <el-tag :type="scope.row.memberOnly ? 'warning' : 'info'" size="mini">
              {{ scope.row.memberOnly ? 'Yes' : 'No' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Status" width="90">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button type="text" size="small" @click="handleViewProducts(scope.row)">Linked Products</el-button>
            <el-button type="text" size="small" style="color: #F56C6C;" @click="handleDelete(scope.row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- pagination -->
      <div class="pagination-container">
        <el-pagination
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- add/edit dialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" @close="handleDialogClose">
      <el-form ref="promotionForm" :model="promotionForm" :rules="promotionRules" label-width="100px" size="small">
        <el-form-item label="Promotion Name" prop="promotionName">
          <el-input v-model="promotionForm.promotionName" placeholder="Enter promotion name" />
        </el-form-item>
        <el-form-item label="Promotion Type" prop="promotionType">
          <el-select v-model="promotionForm.promotionType" placeholder="Select promotion type" style="width: 200px;" @change="handlePromotionTypeChange">
            <el-option label="Percentage" :value="1" />
            <el-option label="Cash Discount" :value="2" />
            <el-option label="Buy & Get" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="promotionForm.promotionType === 1" label="Discount Rate" prop="discountRate">
          <el-input v-model.number="promotionForm.discountRate" placeholder="e.g. 0.85 equals 15% off" type="number" step="0.01" min="0.01" max="0.99" style="width: 200px;" />
          <span class="tip-text">Enter 0.01-0.99 (0.85 = 15% off)</span>
        </el-form-item>
        <el-form-item v-if="promotionForm.promotionType === 2" label="Discount Amount" prop="discountAmount">
          <el-input v-model.number="promotionForm.discountAmount" placeholder="Enter discount amount" type="number" style="width: 200px;">
            <template slot="append">¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="Start Time" prop="startTime">
          <el-date-picker
            v-model="promotionForm.startTime"
            type="datetime"
            placeholder="Choose start time"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="End Time" prop="endTime">
          <el-date-picker
            v-model="promotionForm.endTime"
            type="datetime"
            placeholder="Choose end time"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="Members Only" prop="memberOnly">
          <el-switch v-model="promotionForm.memberOnly" />
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-select v-model="promotionForm.status" placeholder="Select status" style="width: 200px;">
            <el-option label="Not Started" :value="0" />
            <el-option label="Active" :value="1" />
            <el-option label="Ended" :value="2" />
            <el-option label="Canceled" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="Description" prop="description">
          <el-input v-model="promotionForm.description" type="textarea" :rows="3" placeholder="Enter description" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">Confirm</el-button>
      </div>
    </el-dialog>

    <!-- linked products dialog -->
    <el-dialog title="Linked Products" :visible.sync="productDialogVisible" width="800px">
      <div class="product-dialog-content">
        <el-transfer
          v-model="selectedProductIds"
          :data="productOptions"
          :titles="['Available', 'Linked']"
          filterable
          filter-placeholder="Search products"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="productDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSaveProducts">Save</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPromotionList, addPromotion, updatePromotion, deletePromotion } from '@/api/marketing'
import { getProductList } from '@/api/product'

export default {
  name: 'PromotionList',
  data() {
    return {
      loading: false,
      submitting: false,
      promotionList: [],
      searchForm: {
        promotionName: '',
        promotionType: null,
        status: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      promotionForm: {
        promotionId: null,
        promotionName: '',
        promotionType: 1,
        discountRate: 0.9,
        discountAmount: 0,
        startTime: '',
        endTime: '',
        memberOnly: false,
        status: 0,
        description: ''
      },
      promotionRules: {
        promotionName: [
          { required: true, message: 'Please enter the promotion name', trigger: 'blur' },
          { min: 2, max: 50, message: 'Length must be between 2 and 50 characters', trigger: 'blur' }
        ],
        promotionType: [
          { required: true, message: 'Please select a promotion type', trigger: 'change' }
        ],
        discountRate: [
          { required: true, message: 'Please enter the discount rate', trigger: 'blur' }
        ],
        discountAmount: [
          { required: true, message: 'Please enter the discount amount', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: 'Please select the start time', trigger: 'change' }
        ],
        endTime: [
          { required: true, message: 'Please select the end time', trigger: 'change' }
        ],
        status: [
          { required: true, message: 'Please select a status', trigger: 'change' }
        ]
      },
      // linked products
      productDialogVisible: false,
      productOptions: [],
      selectedProductIds: [],
      currentPromotionId: null
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }
        const res = await getPromotionList(params)
        
        /* mock 数据备用
        const mockData = [
          { promotionId: 'PRO001', promotionName: '双十二苹果大促销', promotionType: 1, discountRate: 0.8, startTime: '2023-12-10 00:00:00', endTime: '2023-12-12 23:59:59', memberOnly: false, status: 1 },
          { promotionId: 'PRO002', promotionName: '会员专属满减活动', promotionType: 2, discountAmount: 50, startTime: '2023-12-01 00:00:00', endTime: '2023-12-31 23:59:59', memberOnly: true, status: 1 },
          { promotionId: 'PRO003', promotionName: '买二送一牛奶特惠', promotionType: 3, startTime: '2023-12-05 00:00:00', endTime: '2023-12-15 23:59:59', memberOnly: false, status: 2 }
        ]
        */
        
        if (res.status === 200) {
          this.promotionList = res.data.list || res.data || []
          this.pagination.total = res.data.total || this.promotionList.length
        } else {
          this.$message.error(res.msg || 'Failed to fetch promotion list')
        }
      } catch (error) {
        console.error('Failed to fetch promotion list:', error)
        this.$message.error('Failed to fetch promotion list')
      } finally {
        this.loading = false
      }
    },

    handleSearch() {
      this.pagination.currentPage = 1
      this.fetchData()
    },

    resetSearch() {
      this.searchForm = {
        promotionName: '',
        promotionType: null,
        status: null
      }
      this.handleSearch()
    },

    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.fetchData()
    },

    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.fetchData()
    },

    handleAdd() {
      this.dialogTitle = 'Create Promotion'
      this.isEdit = false
      this.promotionForm = {
        promotionId: null,
        promotionName: '',
        promotionType: 1,
        discountRate: 0.9,
        discountAmount: 0,
        startTime: '',
        endTime: '',
        memberOnly: false,
        status: 0,
        description: ''
      }
      this.dialogVisible = true
    },

    handleEdit(row) {
      this.dialogTitle = 'Edit Promotion'
      this.isEdit = true
      this.promotionForm = {
        promotionId: row.promotionId,
        promotionName: row.promotionName,
        promotionType: row.promotionType,
        discountRate: row.discountRate || 0.9,
        discountAmount: row.discountAmount || 0,
        startTime: row.startTime,
        endTime: row.endTime,
        memberOnly: row.memberOnly || false,
        status: row.status,
        description: row.description || ''
      }
      this.dialogVisible = true
    },

    handlePromotionTypeChange(type) {
      if (type === 1) {
        this.promotionForm.discountRate = 0.9
        this.promotionForm.discountAmount = 0
      } else if (type === 2) {
        this.promotionForm.discountRate = 0
        this.promotionForm.discountAmount = 50
      } else {
        this.promotionForm.discountRate = 0
        this.promotionForm.discountAmount = 0
      }
    },

    async handleDelete(row) {
      try {
        await this.$confirm(`Delete promotion "${row.promotionName}"?`, 'Reminder', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        })

        const res = await deletePromotion(row.promotionId)
        if (res.status === 200) {
          this.$message.success('Deleted successfully')
          this.fetchData()
        } else {
          this.$message.error(res.msg || 'Deletion failed')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Failed to delete promotion:', error)
          this.$message.error('Deletion failed')
        }
      }
    },

    async handleSubmit() {
      this.$refs.promotionForm.validate(async (valid) => {
        if (!valid) return

        this.submitting = true
        try {
          const submitData = { ...this.promotionForm }
          // clean up unnecessary fields based on promotion type
          if (submitData.promotionType !== 1) {
            submitData.discountRate = null
          }
          if (submitData.promotionType !== 2) {
            submitData.discountAmount = null
          }

          let res
          if (this.isEdit) {
            res = await updatePromotion(submitData)
          } else {
            res = await addPromotion(submitData)
          }

          if (res.status === 200) {
            this.$message.success(this.isEdit ? 'Updated successfully' : 'Created successfully')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(res.msg || (this.isEdit ? 'Update failed' : 'Creation failed'))
          }
        } catch (error) {
          console.error('Submit failed:', error)
          this.$message.error('Submit failed')
        } finally {
          this.submitting = false
        }
      })
    },

    handleDialogClose() {
      this.$refs.promotionForm && this.$refs.promotionForm.resetFields()
    },

    async handleViewProducts(row) {
      this.currentPromotionId = row.promotionId
      this.productDialogVisible = true

      try {
        const res = await getProductList({ pageSize: 1000 })
        if (res.status === 200) {
          const products = res.data.list || res.data || []
          this.productOptions = products.map(item => ({
            key: item.productId,
            label: item.productName,
            disabled: false
          }))
        }
      } catch (error) {
        console.error('Failed to fetch product list:', error)
      }
    },

    async handleSaveProducts() {
      try {
        // TODO: call API to save linked products
        this.$message.success('Saved successfully')
        this.productDialogVisible = false
      } catch (error) {
        console.error('Failed to save linked products:', error)
        this.$message.error('Save failed')
      }
    },

    // format discount rate display: 0.85 -> "15% off"
    formatDiscountRate(rate) {
      if (!rate) return '-'
      const percentOff = ((1 - rate) * 100).toFixed(1)
      return `${percentOff}% off`
    },

    getPromotionTypeName(type) {
      const typeMap = {
        1: 'Percentage',
        2: 'Cash Discount',
        3: 'Buy & Get'
      }
      return typeMap[type] || 'Unknown'
    },

    getPromotionTypeTag(type) {
      const tagMap = {
        1: 'success',
        2: 'warning',
        3: 'primary'
      }
      return tagMap[type] || 'info'
    },

    getStatusName(status) {
      const statusMap = {
        0: 'Not Started',
        1: 'Active',
        2: 'Ended',
        3: 'Canceled'
      }
      return statusMap[status] || 'Unknown'
    },

    getStatusTag(status) {
      const tagMap = {
        0: 'info',
        1: 'success',
        2: 'danger',
        3: 'warning'
      }
      return tagMap[status] || 'info'
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

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.tip-text {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}

.product-dialog-content {
  display: flex;
  justify-content: center;
}
</style>
