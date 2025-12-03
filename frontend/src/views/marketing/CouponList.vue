<template>
  <div class="app-container">
    <el-card class="filter-container">
      <div class="filter-form">
        <el-form :inline="true" :model="searchForm" size="small">
          <el-form-item label="Coupon Name">
            <el-input v-model="searchForm.couponName" placeholder="Enter coupon name" clearable style="width: 200px;" @keyup.enter.native="handleSearch" />
          </el-form-item>
          <el-form-item label="Coupon Type">
            <el-select v-model="searchForm.couponType" placeholder="Select type" clearable style="width: 150px;">
              <el-option label="Cash Discount" :value="1" />
              <el-option label="Percentage" :value="2" />
              <el-option label="Points" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="Status">
            <el-select v-model="searchForm.status" placeholder="Select status" clearable style="width: 120px;">
              <el-option label="Inactive" :value="0" />
              <el-option label="Available" :value="1" />
            </el-select>
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
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">Add Coupon</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="couponList"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column prop="couponId" label="Coupon ID" width="120" />
        <el-table-column prop="couponName" label="Coupon Name" min-width="180" show-overflow-tooltip />
        <el-table-column prop="couponType" label="Type" width="100">
          <template slot-scope="scope">
            <el-tag :type="getCouponTypeTag(scope.row.couponType)">
              {{ getCouponTypeText(scope.row.couponType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Discount" width="150">
          <template slot-scope="scope">
            <span v-if="scope.row.couponType === 1">
              Spend ¥{{ scope.row.minAmount }} save ¥{{ scope.row.denomination }}
            </span>
            <span v-else-if="scope.row.couponType === 2">
              {{ formatDiscountRate(scope.row.discountRate) }}
            </span>
            <span v-else>
              {{ scope.row.denomination }} points off
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="minAmount" label="Min Spend" width="100" align="center">
          <template slot-scope="scope">
            ¥{{ scope.row.minAmount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="validDays" label="Valid Days" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.validDays }} days
          </template>
        </el-table-column>
        <el-table-column prop="totalQuantity" label="Issued" width="100" align="center" />
        <el-table-column prop="usedQuantity" label="Redeemed" width="100" align="center" />
        <el-table-column label="Usage Rate" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.totalQuantity > 0 ? ((scope.row.usedQuantity / scope.row.totalQuantity) * 100).toFixed(1) : 0 }}%
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Status" width="90">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Created At" width="160" />
        <el-table-column label="Actions" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">View</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button type="text" size="small" style="color: #F56C6C;" @click="handleDelete(scope.row)" :disabled="scope.row.usedQuantity > 0">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Coupon form dialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" @close="handleDialogClose">
      <el-form ref="couponForm" :model="couponForm" :rules="couponRules" label-width="120px" size="small">
        <el-form-item label="Coupon Name" prop="couponName">
          <el-input v-model="couponForm.couponName" placeholder="Enter coupon name" />
        </el-form-item>
        <el-form-item label="Coupon Type" prop="couponType">
          <el-select v-model="couponForm.couponType" placeholder="Select coupon type" style="width: 200px;" @change="handleCouponTypeChange">
            <el-option label="Cash Discount" :value="1" />
            <el-option label="Percentage" :value="2" />
            <el-option label="Points" :value="3" />
          </el-select>
        </el-form-item>

        <!-- Threshold coupon -->
        <template v-if="couponForm.couponType === 1">
          <el-form-item label="Spend Threshold" prop="minAmount">
            <el-input v-model.number="couponForm.minAmount" placeholder="Enter spend amount" type="number" style="width: 200px;">
              <template slot="append">¥</template>
            </el-input>
          </el-form-item>
          <el-form-item label="Discount Amount" prop="denomination">
            <el-input v-model.number="couponForm.denomination" placeholder="Enter discount amount" type="number" style="width: 200px;">
              <template slot="append">¥</template>
            </el-input>
          </el-form-item>
        </template>

        <!-- Discount coupon -->
        <template v-if="couponForm.couponType === 2">
          <el-form-item label="Discount Rate" prop="discountRate">
            <el-input v-model.number="couponForm.discountRate" placeholder="e.g. 0.85 equals 15% off" type="number" step="0.01" min="0.01" max="0.99" style="width: 200px;">
              <template slot="append">(0.01-0.99)</template>
            </el-input>
            <span class="tip-text">Enter a number between 0.01-0.99 (0.85 = 15% off)</span>
          </el-form-item>
          <el-form-item label="Min Spend" prop="minAmount">
            <el-input v-model.number="couponForm.minAmount" placeholder="Enter minimum spend, 0 for none" type="number" style="width: 200px;">
              <template slot="append">¥</template>
            </el-input>
          </el-form-item>
        </template>

        <!-- Points coupon -->
        <template v-if="couponForm.couponType === 3">
          <el-form-item label="Points Deduction" prop="denomination">
            <el-input v-model.number="couponForm.denomination" placeholder="Enter redeemable points" type="number" style="width: 200px;">
              <template slot="append">pts</template>
            </el-input>
          </el-form-item>
          <el-form-item label="Min Spend" prop="minAmount">
            <el-input v-model.number="couponForm.minAmount" placeholder="Enter minimum spend, 0 for none" type="number" style="width: 200px;">
              <template slot="append">¥</template>
            </el-input>
          </el-form-item>
        </template>

        <el-form-item label="Valid Days" prop="validDays">
          <el-input v-model.number="couponForm.validDays" placeholder="Enter number of valid days" type="number" style="width: 200px;">
            <template slot="append">days</template>
          </el-input>
          <span class="tip-text">Valid period after the user claims it</span>
        </el-form-item>

        <el-form-item label="Issued Quantity" prop="totalQuantity">
          <el-input v-model.number="couponForm.totalQuantity" placeholder="Enter number to issue" type="number" style="width: 200px;">
            <template slot="append">pcs</template>
          </el-input>
        </el-form-item>

        <el-form-item label="Status" prop="status">
          <el-radio-group v-model="couponForm.status">
            <el-radio :label="0">Inactive</el-radio>
            <el-radio :label="1">Available</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">Confirm</el-button>
      </div>
    </el-dialog>

    <!-- Coupon details dialog -->
    <el-dialog title="Coupon Details" :visible.sync="detailVisible" width="600px">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="Coupon ID">{{ currentCoupon.couponId }}</el-descriptions-item>
        <el-descriptions-item label="Coupon Name">{{ currentCoupon.couponName }}</el-descriptions-item>
        <el-descriptions-item label="Coupon Type">
          <el-tag :type="getCouponTypeTag(currentCoupon.couponType)">
            {{ getCouponTypeText(currentCoupon.couponType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Discount">
          <span v-if="currentCoupon.couponType === 1">Spend ¥{{ currentCoupon.minAmount }} save ¥{{ currentCoupon.denomination }}</span>
          <span v-else-if="currentCoupon.couponType === 2">{{ formatDiscountRate(currentCoupon.discountRate) }}</span>
          <span v-else>{{ currentCoupon.denomination }} points off</span>
        </el-descriptions-item>
        <el-descriptions-item label="Min Spend">¥{{ currentCoupon.minAmount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="Valid Days">{{ currentCoupon.validDays }} days</el-descriptions-item>
        <el-descriptions-item label="Issued">{{ currentCoupon.totalQuantity }} pcs</el-descriptions-item>
        <el-descriptions-item label="Redeemed">{{ currentCoupon.usedQuantity }} pcs</el-descriptions-item>
        <el-descriptions-item label="Usage Rate">
          {{ currentCoupon.totalQuantity > 0 ? ((currentCoupon.usedQuantity / currentCoupon.totalQuantity) * 100).toFixed(1) : 0 }}%
        </el-descriptions-item>
        <el-descriptions-item label="Status">
          <el-tag :type="getStatusType(currentCoupon.status)">
            {{ getStatusText(currentCoupon.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Created At">{{ currentCoupon.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">Close</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCouponList,
  getCoupon,
  addCoupon,
  updateCoupon,
  deleteCoupon
} from '@/api/marketing'

export default {
  name: 'CouponList',
  data() {
    return {
      loading: false,
      submitting: false,
      couponList: [],
      searchForm: {
        couponName: '',
        couponType: null,
        status: null
      },
      dialogVisible: false,
      detailVisible: false,
      dialogTitle: '',
      isEdit: false,
      currentCoupon: {},
      couponForm: {
        couponId: '',
        couponName: '',
        couponType: 1,
        denomination: 0,
        discountRate: 0,
        minAmount: 0,
        validDays: 30,
        totalQuantity: 0,
        usedQuantity: 0,
        status: 1
      },
      couponRules: {
        couponName: [
          { required: true, message: 'Please enter the coupon name', trigger: 'blur' }
        ],
        couponType: [
          { required: true, message: 'Please select a coupon type', trigger: 'change' }
        ],
        denomination: [
          { required: true, message: 'Please enter the face value or deduction amount', trigger: 'blur' }
        ],
        discountRate: [
          { required: true, message: 'Please enter the discount rate', trigger: 'blur' }
        ],
        minAmount: [
          { required: true, message: 'Please enter the minimum spend requirement', trigger: 'blur' }
        ],
        validDays: [
          { required: true, message: 'Please enter the valid days', trigger: 'blur' }
        ],
        totalQuantity: [
          { required: true, message: 'Please enter the issued quantity', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const response = await getCouponList(this.searchForm)
        
        /* mock 数据备用
        const mockData = [
          { couponId: 'COUP001', couponName: '新用户专享10元优惠券', couponType: 1, denomination: 10, minAmount: 50, validDays: 30, totalQuantity: 1000, usedQuantity: 234, status: 1 },
          { couponId: 'COUP002', couponName: '满100减20优惠券', couponType: 1, denomination: 20, minAmount: 100, validDays: 15, totalQuantity: 500, usedQuantity: 89, status: 1 },
          { couponId: 'COUP003', couponName: '生日专属8.5折券', couponType: 2, discountRate: 0.85, minAmount: 0, validDays: 7, totalQuantity: 200, usedQuantity: 45, status: 1 }
        ]
        */
        
        if (response.status === 200) {
          this.couponList = response.data.list || response.data || []
        } else {
          this.$message.error(response.msg || 'Failed to fetch coupon list')
        }
      } catch (error) {
        console.error('Failed to fetch coupon list:', error)
        this.$message.error('Failed to fetch coupon list')
      } finally {
        this.loading = false
      }
    },

    handleSearch() {
      this.fetchData()
    },

    resetSearch() {
      this.searchForm = {
        couponName: '',
        couponType: null,
        status: null
      }
      this.fetchData()
    },

    handleAdd() {
      this.dialogTitle = 'Create Coupon'
      this.isEdit = false
      this.couponForm = {
        couponId: '',
        couponName: '',
        couponType: 1,
        denomination: 0,
        discountRate: 0,
        minAmount: 0,
        validDays: 30,
        totalQuantity: 0,
        usedQuantity: 0,
        status: 1
      }
      this.handleCouponTypeChange(1)
      this.dialogVisible = true
    },

    async handleView(row) {
      this.currentCoupon = row
      this.detailVisible = true
    },

    async handleEdit(row) {
      this.dialogTitle = 'Edit Coupon'
      this.isEdit = true

      try {
        const response = await getCoupon(row.couponId)
        if (response.status === 200) {
          this.couponForm = { ...response.data }
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || 'Failed to fetch coupon details')
        }
      } catch (error) {
        console.error('Failed to fetch coupon details:', error)
        this.$message.error('Failed to fetch coupon details')
        // If fetch fails, use row data directly
        this.couponForm = { ...row }
        this.dialogVisible = true
      }
    },

    async handleDelete(row) {
      try {
        await this.$confirm('Delete this coupon? Claimed coupons will become invalid.', 'Reminder', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        })

        const response = await deleteCoupon(row.couponId)
        if (response.status === 200) {
          this.$message.success('Deleted successfully')
          this.fetchData()
        } else {
          this.$message.error(response.msg || 'Deletion failed')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Failed to delete coupon:', error)
          this.$message.error('Deletion failed')
        }
      }
    },

    handleCouponTypeChange(type) {
      // Reset related fields
      this.couponForm.denomination = 0
      this.couponForm.discountRate = 0
      this.couponForm.minAmount = 0

      // Set defaults
      if (type === 1) { // Threshold coupon
        this.couponForm.minAmount = 100
        this.couponForm.denomination = 10
      } else if (type === 2) { // Discount coupon
        this.couponForm.discountRate = 0.85
        this.couponForm.minAmount = 0
      } else if (type === 3) { // Points coupon
        this.couponForm.denomination = 100
        this.couponForm.minAmount = 50
      }
    },

    async handleSubmit() {
      this.$refs.couponForm.validate(async (valid) => {
        if (!valid) return

        // Validate valid days
        if (this.couponForm.validDays <= 0) {
          this.$message.error('Valid days must be greater than 0')
          return
        }

        // Validate issue quantity
        if (this.couponForm.totalQuantity <= 0) {
          this.$message.error('Issued quantity must be greater than 0')
          return
        }

        // Validate discount rate range
        if (this.couponForm.couponType === 2) {
          if (this.couponForm.discountRate <= 0 || this.couponForm.discountRate >= 1) {
            this.$message.error('Discount rate must be between 0.01 and 0.99')
            return
          }
        }

        this.submitting = true
        try {
          // build submit data, clean up fields based on coupon type
          const submitData = { ...this.couponForm }
          if (submitData.couponType !== 2) {
            submitData.discountRate = 0
          }
          if (submitData.couponType === 2) {
            submitData.denomination = 0
          }

          let response
          if (this.isEdit) {
            response = await updateCoupon(submitData)
          } else {
            response = await addCoupon(submitData)
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
      this.$refs.couponForm && this.$refs.couponForm.resetFields()
    },

    // format discount rate display: 0.85 -> "15% off"
    formatDiscountRate(rate) {
      if (!rate) return '-'
      const percentOff = ((1 - rate) * 100).toFixed(1)
      return `${percentOff}% off`
    },

    getCouponTypeTag(type) {
      const typeMap = {
        1: 'primary',   // Threshold
        2: 'success',   // Discount
        3: 'warning'    // Points
      }
      return typeMap[type] || 'info'
    },

    getCouponTypeText(type) {
      const typeMap = {
        1: 'Cash Discount',
        2: 'Percentage',
        3: 'Points'
      }
      return typeMap[type] || 'Unknown'
    },

    getStatusType(status) {
      const statusMap = {
        0: 'danger',    // Inactive
        1: 'success'    // Available
      }
      return statusMap[status] || 'info'
    },

    getStatusText(status) {
      const statusMap = {
        0: 'Inactive',
        1: 'Available'
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

.tip-text {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}
</style>
