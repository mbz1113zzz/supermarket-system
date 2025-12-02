<template>
  <div class="app-container">
    <el-card class="filter-container">
      <div class="filter-form">
        <el-form :inline="true" :model="searchForm" size="small">
          <el-form-item label="Supplier Name">
            <el-input v-model="searchForm.supplierName" placeholder="Enter supplier name" clearable style="width: 200px;" @keyup.enter.native="handleSearch" />
          </el-form-item>
          <el-form-item label="Contact Person">
            <el-input v-model="searchForm.contactName" placeholder="Enter contact name" clearable style="width: 180px;" @keyup.enter.native="handleSearch" />
          </el-form-item>
          <el-form-item label="Status">
            <el-select v-model="searchForm.status" placeholder="Select status" clearable style="width: 120px;">
              <el-option label="Active" :value="1" />
              <el-option label="Disabled" :value="0" />
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
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">Add Supplier</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="supplierList"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column prop="supplierId" label="Supplier ID" width="120" />
        <el-table-column prop="supplierName" label="Supplier Name" min-width="180" show-overflow-tooltip />
        <el-table-column prop="contactPerson" label="Contact" width="100" />
        <el-table-column prop="contactPhone" label="Phone" width="130" />
        <el-table-column prop="creditRating" label="Credit Rating" width="90">
          <template slot-scope="scope">
            <el-tag :type="getCreditRatingType(scope.row.creditRating)">
              {{ getCreditRatingLabel(scope.row.creditRating) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Status" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? 'Active' : 'Disabled' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Created At" width="160" />
        <el-table-column label="Actions" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">View</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button
              type="text"
              size="small"
              :style="{ color: scope.row.status === 1 ? '#F56C6C' : '#67C23A' }"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.status === 1 ? 'Disable' : 'Activate' }}
            </el-button>
            <el-button type="text" size="small" style="color: #F56C6C;" @click="handleDelete(scope.row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- supplier form dialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" @close="handleDialogClose">
      <el-form ref="supplierForm" :model="supplierForm" :rules="supplierRules" label-width="120px" size="small">
        <el-form-item label="Supplier Name" prop="supplierName">
          <el-input v-model="supplierForm.supplierName" placeholder="Enter supplier name" />
        </el-form-item>
        <el-form-item label="Contact" prop="contactPerson">
          <el-input v-model="supplierForm.contactPerson" placeholder="Enter contact name" />
        </el-form-item>
        <el-form-item label="Phone" prop="contactPhone">
          <el-input v-model="supplierForm.contactPhone" placeholder="Enter phone number" />
        </el-form-item>
        <el-form-item label="Email" prop="contactEmail">
          <el-input v-model="supplierForm.contactEmail" placeholder="Enter email address" />
        </el-form-item>
        <el-form-item label="Address" prop="address">
          <el-input v-model="supplierForm.address" type="textarea" :rows="3" placeholder="Enter address" />
        </el-form-item>
        <el-form-item label="Credit Rating" prop="creditRating">
          <el-select v-model="supplierForm.creditRating" placeholder="Select credit rating" style="width: 200px;">
            <el-option label="5 Stars (Excellent)" :value="5" />
            <el-option label="4 Stars (Great)" :value="4" />
            <el-option label="3 Stars (Good)" :value="3" />
            <el-option label="2 Stars (Fair)" :value="2" />
            <el-option label="1 Star (Poor)" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="Status" prop="status" v-if="isEdit">
          <el-radio-group v-model="supplierForm.status">
            <el-radio :label="1">Active</el-radio>
            <el-radio :label="0">Disabled</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">Confirm</el-button>
      </div>
    </el-dialog>

    <!-- supplier details dialog -->
    <el-dialog title="Supplier Details" :visible.sync="detailVisible" width="600px">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="Supplier ID">{{ currentSupplier.supplierId }}</el-descriptions-item>
        <el-descriptions-item label="Supplier Name">{{ currentSupplier.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="Contact">{{ currentSupplier.contactPerson }}</el-descriptions-item>
        <el-descriptions-item label="Phone">{{ currentSupplier.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="Email">{{ currentSupplier.contactEmail }}</el-descriptions-item>
        <el-descriptions-item label="Credit Rating">
          <el-tag :type="getCreditRatingType(currentSupplier.creditRating)">
            {{ getCreditRatingLabel(currentSupplier.creditRating) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Status" :span="2">
          <el-tag :type="currentSupplier.status === 1 ? 'success' : 'danger'">
            {{ currentSupplier.status === 1 ? 'Active' : 'Disabled' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Address" :span="2">{{ currentSupplier.address }}</el-descriptions-item>
        <el-descriptions-item label="Created At" :span="2">{{ currentSupplier.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">Close</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSupplierList,
  getSupplier,
  addSupplier,
  updateSupplier,
  deleteSupplier,
  updateSupplierStatus
} from '@/api/procurement'

export default {
  name: 'SupplierList',
  data() {
    return {
      loading: false,
      submitting: false,
      supplierList: [],
      searchForm: {
        supplierName: '',
        contactName: '',
        status: null
      },
      dialogVisible: false,
      detailVisible: false,
      dialogTitle: '',
      isEdit: false,
      currentSupplier: {},
      supplierForm: {
        supplierId: '',
        supplierName: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        address: '',
        creditRating: 3,
        status: 1
      },
      supplierRules: {
        supplierName: [
          { required: true, message: 'Please enter the supplier name', trigger: 'blur' }
        ],
        contactPerson: [
          { required: true, message: 'Please enter the contact name', trigger: 'blur' }
        ],
        contactPhone: [
          { required: true, message: 'Please enter the phone number', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: 'Please enter a valid mobile number', trigger: 'blur' }
        ],
        contactEmail: [
          { pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/, message: 'Please enter a valid email address', trigger: 'blur' }
        ],
        address: [
          { required: true, message: 'Please enter the address', trigger: 'blur' }
        ],
        creditRating: [
          { required: true, message: 'Please select a credit rating', trigger: 'change' }
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
        const response = await getSupplierList(this.searchForm)
        
        /* mock 数据备用
        const mockData = [
          { supplierId: 'SUP001', supplierName: '北京农产品批发公司', contactPerson: '张经理', contactPhone: '13800138001', status: 1, creditRating: 5 },
          { supplierId: 'SUP002', supplierName: '上海食品配送中心', contactPerson: '李经理', contactPhone: '13900139002', status: 1, creditRating: 5 },
          { supplierId: 'SUP003', supplierName: '广州水果批发市场', contactPerson: '王老板', contactPhone: '13700137003', status: 1, creditRating: 4 }
        ]
        */

        if (response.status === 200) {
          this.supplierList = response.data
        } else {
          this.$message.error(response.msg || 'Failed to fetch supplier list')
        }
      } catch (error) {
        console.error('Failed to fetch supplier list:', error)
        this.$message.error('Failed to fetch supplier list')
      } finally {
        this.loading = false
      }
    },

    handleSearch() {
      this.fetchData()
    },

    resetSearch() {
      this.searchForm = {
        supplierName: '',
        contactName: '',
        status: null
      }
      this.fetchData()
    },

    handleAdd() {
      this.dialogTitle = 'Add Supplier'
      this.isEdit = false
      this.supplierForm = {
        supplierId: '',
        supplierName: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        address: '',
        creditRating: '',
        status: 1
      }
      this.dialogVisible = true
    },

    async handleView(row) {
      this.currentSupplier = row
      this.detailVisible = true
    },

    async handleEdit(row) {
      this.dialogTitle = 'Edit Supplier'
      this.isEdit = true

      try {
        const response = await getSupplier(row.supplierId)
        if (response.status === 200) {
          this.supplierForm = { ...response.data }
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || 'Failed to fetch supplier details')
        }
      } catch (error) {
        console.error('Failed to fetch supplier details:', error)
        this.$message.error('Failed to fetch supplier details')
        // if fetch fails, just use the row data for editing
        this.supplierForm = { ...row }
        this.dialogVisible = true
      }
    },

    async handleDelete(row) {
      try {
        await this.$confirm('Are you sure you want to delete this supplier?', 'Reminder', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        })

        const response = await deleteSupplier(row.supplierId)
        if (response.status === 200) {
          this.$message.success('Deleted successfully')
          this.fetchData()
        } else {
          this.$message.error(response.msg || 'Deletion failed')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Failed to delete supplier:', error)
          this.$message.error('Deletion failed')
        }
      }
    },

    async handleToggleStatus(row) {
      const action = row.status === 1 ? 'Disable' : 'Activate'
      const newStatus = row.status === 1 ? 0 : 1

      try {
        await this.$confirm(`Are you sure you want to ${action.toLowerCase()} this supplier?`, 'Reminder', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        })

        const response = await updateSupplierStatus(row.supplierId, newStatus)
        if (response.status === 200) {
          this.$message.success(`${action} successful`)
          this.fetchData()
        } else {
          this.$message.error(response.msg || `${action} failed`)
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error(`Failed to ${action.toLowerCase()} supplier:`, error)
          this.$message.error(`${action} failed`)
        }
      }
    },

    async handleSubmit() {
      this.$refs.supplierForm.validate(async (valid) => {
        if (!valid) return

        this.submitting = true
        try {
          let response
          if (this.isEdit) {
            response = await updateSupplier(this.supplierForm)
          } else {
            response = await addSupplier(this.supplierForm)
          }

          if (response.status === 200) {
            this.$message.success(response.msg || (this.isEdit ? 'Updated successfully' : 'Added successfully'))
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(response.msg || (this.isEdit ? 'Update failed' : 'Create failed'))
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
      this.$refs.supplierForm && this.$refs.supplierForm.resetFields()
    },

    getCreditRatingType(rating) {
      const ratingMap = {
        5: 'success',
        4: 'success',
        3: 'warning',
        2: 'warning',
        1: 'danger'
      }
      return ratingMap[rating] || 'info'
    },

    getCreditRatingLabel(rating) {
      const labelMap = {
        5: '5 Stars',
        4: '4 Stars',
        3: '3 Stars',
        2: '2 Stars',
        1: '1 Star'
      }
      return labelMap[rating] || 'Unknown'
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

.card-info {
  line-height: 1.5;
}
</style>