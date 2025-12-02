<template>
  <div class="app-container">
    <el-card class="table-container">
      <div class="operation-container">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">Add Tier</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tierList"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column type="index" label="No." width="80" align="center" />
        <el-table-column prop="tierName" label="Tier Name" width="150" />
        <el-table-column prop="minPoints" label="Minimum Points" width="150" align="right">
          <template slot-scope="scope">
            <span class="points-value">{{ scope.row.minPoints }} pts</span>
          </template>
        </el-table-column>
        <el-table-column prop="discountRate" label="Discount Rate" width="150" align="right">
          <template slot-scope="scope">
            <span class="discount-value">{{ (scope.row.discountRate * 100).toFixed(1) }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="memberCount" label="Members" width="120" align="center" />
        <el-table-column prop="description" label="Tier Description" min-width="200" />
        <el-table-column label="Status" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
              {{ scope.row.status === 'active' ? 'Active' : 'Inactive' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button
              type="text"
              size="small"
              :icon="scope.row.status === 'active' ? 'el-icon-video-pause' : 'el-icon-video-play'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.status === 'active' ? 'Disable' : 'Enable' }}
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-delete"
              style="color: #F56C6C"
              @click="handleDelete(scope.row)"
            >
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- add/edit tier dialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" :close-on-click-modal="false">
      <el-form ref="tierForm" :model="tierForm" :rules="tierRules" label-width="120px" size="small">
        <el-form-item label="Tier Name" prop="tierName">
          <el-input v-model="tierForm.tierName" placeholder="Enter tier name" />
        </el-form-item>
        <el-form-item label="Minimum Points" prop="minPoints">
          <el-input-number
            v-model="tierForm.minPoints"
            :min="0"
            :max="999999"
            placeholder="Enter minimum points"
            style="width: 100%"
          />
          <div class="form-tip">Members who reach these points will upgrade to this tier.</div>
        </el-form-item>
        <el-form-item label="Discount Rate" prop="discountRate">
          <el-input-number
            v-model="tierForm.discountRate"
            :min="0.1"
            :max="1.0"
            :step="0.1"
            :precision="1"
            placeholder="Enter discount rate"
            style="width: 100%"
          />
          <div class="form-tip">Example: 0.8 means customers get 20% off.</div>
        </el-form-item>
        <el-form-item label="Tier Description" prop="description">
          <el-input
            v-model="tierForm.description"
            type="textarea"
            :rows="3"
            placeholder="Enter a tier description"
          />
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-radio-group v-model="tierForm.status">
            <el-radio label="active">Active</el-radio>
            <el-radio label="inactive">Inactive</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">Confirm</el-button>
      </div>
    </el-dialog>

    <!-- tier upgrade rules explanation -->
    <el-card style="margin-top: 20px;">
      <div slot="header" class="clearfix">
        <span>Tier Upgrade Rules</span>
      </div>
      <div class="tier-rules">
        <el-steps :active="3" direction="vertical" finish-status="success">
          <el-step
            v-for="tier in sortedTierList"
            :key="tier.id"
            :title="`${tier.tierName} - ${tier.minPoints} pts`"
            :description="tier.description || 'Enjoy a ' + (tier.discountRate * 100).toFixed(1) + '% rate'"
          />
        </el-steps>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'MemberTier',
  data() {
    return {
      loading: false,
      submitting: false,
      tierList: [
        {
          id: 1,
          tierName: 'Regular Member',
          minPoints: 0,
          discountRate: 1.0,
          memberCount: 680,
          description: 'Regular members enjoy standard services.',
          status: 'active'
        },
        {
          id: 2,
          tierName: 'Silver Member',
          minPoints: 1000,
          discountRate: 0.95,
          memberCount: 420,
          description: 'Silver members receive a 5% discount.',
          status: 'active'
        },
        {
          id: 3,
          tierName: 'Gold Member',
          minPoints: 5000,
          discountRate: 0.9,
          memberCount: 148,
          description: 'Gold members receive a 10% discount.',
          status: 'active'
        },
        {
          id: 4,
          tierName: 'Diamond Member',
          minPoints: 10000,
          discountRate: 0.85,
          memberCount: 32,
          description: 'Diamond members receive 15% off and dedicated support.',
          status: 'active'
        }
      ],
      dialogVisible: false,
      isEdit: false,
      dialogTitle: '',
      tierForm: {
        tierName: '',
        minPoints: 0,
        discountRate: 1.0,
        description: '',
        status: 'active'
      },
      tierRules: {
        tierName: [
          { required: true, message: 'Please enter a tier name', trigger: 'blur' },
          { min: 2, max: 20, message: 'Length must be between 2 and 20 characters', trigger: 'blur' }
        ],
        minPoints: [
          { required: true, message: 'Please enter the minimum points', trigger: 'blur' },
          { type: 'number', min: 0, message: 'Points must be zero or greater', trigger: 'blur' }
        ],
        discountRate: [
          { required: true, message: 'Please enter a discount rate', trigger: 'blur' },
          { type: 'number', min: 0.1, max: 1.0, message: 'Discount rate must be between 0.1 and 1.0', trigger: 'blur' }
        ],
        status: [
          { required: true, message: 'Please select a status', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    sortedTierList() {
      return [...this.tierList].sort((a, b) => a.minPoints - b.minPoints)
    }
  },
  created() {
    this.fetchTierList()
  },
  methods: {
    async fetchTierList() {
      this.loading = true
      try {
        // call actual API to get tier list
        // const response = await tierApi.getTierList()
        // this.tierList = response.data

        // simulate API call delay
        await new Promise(resolve => setTimeout(resolve, 500))
      } catch (error) {
        this.$message.error('Failed to retrieve tier list')
        console.error('Failed to retrieve tier list:', error)
      } finally {
        this.loading = false
      }
    },

    handleAdd() {
      this.isEdit = false
      this.dialogTitle = 'Add Tier'
      this.dialogVisible = true
      this.resetForm()
    },

    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = 'Edit Tier'
      this.dialogVisible = true
      this.tierForm = {
        id: row.id,
        tierName: row.tierName,
        minPoints: row.minPoints,
        discountRate: row.discountRate,
        description: row.description,
        status: row.status
      }
    },

    handleToggleStatus(row) {
      const action = row.status === 'active' ? 'Disable' : 'Enable'
      this.$confirm(`Are you sure you want to ${action.toLowerCase()} tier ${row.tierName}?`, 'Notice', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        // call toggle status API
        const newStatus = row.status === 'active' ? 'inactive' : 'active'
        const index = this.tierList.findIndex(item => item.id === row.id)
        if (index !== -1) {
          this.tierList[index].status = newStatus
        }
        this.$message.success(`${action}d successfully`)
      }).catch(() => {
        this.$message.info('Operation canceled')
      })
    },

    handleDelete(row) {
      this.$confirm(`Delete tier ${row.tierName}? This action cannot be undone.`, 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        // call delete API
        const index = this.tierList.findIndex(item => item.id === row.id)
        if (index !== -1) {
          this.tierList.splice(index, 1)
        }
        this.$message.success('Deleted successfully')
      }).catch(() => {
        this.$message.info('Deletion canceled')
      })
    },

    submitForm() {
      this.$refs.tierForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            // check if points requirement is duplicate
            const duplicate = this.tierList.find(tier =>
              tier.minPoints === this.tierForm.minPoints &&
              (!this.isEdit || tier.id !== this.tierForm.id)
            )

            if (duplicate) {
              this.$message.error('Minimum points must be unique')
              this.submitting = false
              return
            }

            // call add/edit API
            await new Promise(resolve => setTimeout(resolve, 1000)) // mock API call

            if (this.isEdit) {
              const index = this.tierList.findIndex(item => item.id === this.tierForm.id)
              if (index !== -1) {
                this.tierList[index] = { ...this.tierForm }
              }
              this.$message.success('Tier updated successfully')
            } else {
              const newTier = {
                ...this.tierForm,
                id: Date.now(),
                memberCount: 0
              }
              this.tierList.push(newTier)
              this.$message.success('Tier created successfully')
            }

            this.dialogVisible = false
          } catch (error) {
            this.$message.error(this.isEdit ? 'Update failed' : 'Creation failed')
          } finally {
            this.submitting = false
          }
        }
      })
    },

    resetForm() {
      this.tierForm = {
        tierName: '',
        minPoints: 0,
        discountRate: 1.0,
        description: '',
        status: 'active'
      }
      if (this.$refs.tierForm) {
        this.$refs.tierForm.clearValidate()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.operation-container {
  margin-bottom: 20px;
}

.points-value {
  color: #E6A23C;
  font-weight: bold;
}

.discount-value {
  color: #67C23A;
  font-weight: bold;
}

.dialog-footer {
  text-align: right;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.tier-rules {
  padding: 20px 0;
}
</style>