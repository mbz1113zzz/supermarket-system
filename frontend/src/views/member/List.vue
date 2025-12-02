<template>
  <div class="app-container">
    <el-card class="filter-container">
      <div class="filter-form">
        <el-form :inline="true" :model="searchForm" size="small">
          <el-form-item label="Member Card Number">
            <el-input v-model="searchForm.memberId" placeholder="Enter card number" clearable style="width: 200px;" @keyup.enter.native="handleSearch" />
          </el-form-item>
          <el-form-item label="Name">
            <el-input v-model="searchForm.name" placeholder="Enter name" clearable style="width: 200px;" @keyup.enter.native="handleSearch" />
          </el-form-item>
          <el-form-item label="Birthday Filter">
            <el-select v-model="searchForm.birthdayQuery" placeholder="Select" clearable style="width: 150px;">
              <el-option label="No limit" value="" />
              <el-option label="Birthday today" value="0" />
              <el-option label="Birthday within 7 days" value="7" />
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
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">Add Member</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="memberList"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column prop="memberId" label="Member ID" width="120" />
        <el-table-column prop="name" label="Name" width="100" />
        <el-table-column label="Gender" width="80">
          <template slot-scope="scope">
            {{ formatGender(scope.row.sex) }}
          </template>
        </el-table-column>
        <el-table-column prop="birthday" label="Birthday" width="120" />
        <el-table-column label="Member Card Info" min-width="200">
          <template slot-scope="scope">
            <div v-if="scope.row.cards && scope.row.cards.length > 0">
              <div v-for="card in scope.row.cards" :key="card.cardId" class="card-info">
                Card: {{ card.cardId }} | Points: {{ card.integral }} | Balance: {{ (card.balance / 100).toFixed(2) }} CNY
              </div>
            </div>
            <span v-else>No member card</span>
          </template>
        </el-table-column>
        <el-table-column prop="registerTime" label="Registration Time" width="180" />
        <el-table-column label="Actions" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button type="text" size="small" icon="el-icon-view" @click="handleView(scope.row)">View</el-button>
            <el-button type="text" size="small" icon="el-icon-delete" style="color: #F56C6C" @click="handleDelete(scope.row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          :current-page="pagination.page"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="pagination.size"
          :total="pagination.total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- add/edit dialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" :close-on-click-modal="false">
      <el-form ref="memberForm" :model="memberForm" :rules="memberRules" label-width="100px" size="small">
        <el-form-item label="Member ID" prop="memberId">
          <el-input
            v-model="memberForm.memberId"
            placeholder="Leave blank to auto-generate"
            :disabled="isEdit"
          />
        </el-form-item>
        <el-form-item label="Name" prop="name">
          <el-input v-model="memberForm.name" placeholder="Enter name" />
        </el-form-item>
        <el-form-item label="Gender" prop="sex">
          <el-radio-group v-model="memberForm.sex">
            <el-radio label="男">Male</el-radio>
            <el-radio label="女">Female</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Birthday" prop="birthday">
          <el-date-picker
            v-model="memberForm.birthday"
            type="date"
            placeholder="Select birthday"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="Password" prop="password" v-if="!isEdit">
          <el-input v-model="memberForm.password" type="password" placeholder="Enter password" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">Confirm</el-button>
      </div>
    </el-dialog>

    <!-- view details dialog -->
    <el-dialog title="Member Details" :visible.sync="viewDialogVisible" width="600px">
      <div v-if="currentMember" class="member-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Member ID">{{ currentMember.memberId }}</el-descriptions-item>
          <el-descriptions-item label="Name">{{ currentMember.name }}</el-descriptions-item>
          <el-descriptions-item label="Gender">{{ formatGender(currentMember.sex) }}</el-descriptions-item>
          <el-descriptions-item label="Birthday">{{ currentMember.birthday }}</el-descriptions-item>
          <el-descriptions-item label="Registration Time">{{ currentMember.registerTime }}</el-descriptions-item>
        </el-descriptions>

        <div style="margin-top: 20px;">
          <h4>Member Card Information</h4>
          <el-table :data="currentMember.cards" border size="small" style="width: 100%">
            <el-table-column prop="cardId" label="Card Number" />
            <el-table-column prop="integral" label="Points" />
            <el-table-column label="Balance">
              <template slot-scope="scope">
                {{ (scope.row.balance / 100).toFixed(2) }} CNY
              </template>
            </el-table-column>
            <el-table-column label="Status">
              <template slot-scope="scope">
                <el-tag :type="scope.row.lose === 0 ? 'success' : 'danger'">
                  {{ scope.row.lose === 0 ? 'Active' : 'Lost' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getMemberList, addMember, updateMember, deleteMember } from '@/api/member'

export default {
  name: 'MemberList',
  data() {
    return {
      loading: false, // loading state
      submitting: false, // submitting state to prevent duplicates
      searchForm: {
        memberId: '',
        name: '',
        birthdayQuery: ''
      },
      memberList: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      viewDialogVisible: false,
      isEdit: false,
      dialogTitle: '',
      memberForm: {
        memberId: '',
        name: '',
        sex: '男',
        birthday: '',
        password: ''
      },
      currentMember: null,
      memberRules: {
        memberId: [
          { min: 3, max: 20, message: 'Length must be between 3 and 20 characters', trigger: 'blur' }
        ],
        name: [
          { required: true, message: 'Please enter a name', trigger: 'blur' },
          { min: 2, max: 20, message: 'Length must be between 2 and 20 characters', trigger: 'blur' }
        ],
        sex: [
          { required: true, message: 'Please select a gender', trigger: 'change' }
        ],
        birthday: [
          { required: true, message: 'Please select a birthday', trigger: 'change' }
        ],
        password: [
          { required: true, message: 'Please enter a password', trigger: 'blur' },
          { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.fetchMemberList()
  },
  methods: {
    // get member list
    // page size API compatibility
    // TODO unify naming
    async fetchMemberList() {
      this.loading = true
      try {
        // param compatibility
        const params = {
          page: this.pagination.page,
          size: this.pagination.size,
          pageIndex: this.pagination.page,
          pageSize: this.pagination.size,
          memberId: this.searchForm.memberId || undefined,
          name: this.searchForm.name || undefined,
          birthdayQuery: this.searchForm.birthdayQuery,
          birthdayRangeDays: this.searchForm.birthdayQuery
        }

        // debug note
        
        // API call
        const response = await getMemberList(params)
        
        // response compatibility
        if (response && response.data) {
          this.memberList = response.data.list || []
          // backend returns pageTotal
          this.pagination.total = response.data.pageTotal || 0
        } else {
          // format warning
          console.warn('Unexpected response format:', response)
          this.memberList = []
        }
      } catch (error) {
        this.$message.error('Failed to retrieve member list')
        console.error('Failed to retrieve member list:', error)
        // error handling
      } finally {
        this.loading = false
      }
    },

    // search
    handleSearch() {
      this.pagination.page = 1 // reset to first page
      this.fetchMemberList()
    },

    // reset filters
    resetSearch() {
      this.searchForm = {
        memberId: '',
        name: '',
        birthdayQuery: '' // empty means no filter
      }
      this.pagination.page = 1
      this.fetchMemberList()
    },

    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.page = 1
      this.fetchMemberList()
    },

    handleCurrentChange(val) {
      this.pagination.page = val
      this.fetchMemberList()
    },

    formatGender(value) {
      if (value === undefined || value === null || value === '') {
        return '-'
      }
      const map = {
        男: 'Male',
        女: 'Female',
        male: 'Male',
        female: 'Female',
        M: 'Male',
        F: 'Female'
      }
      return map[value] || value
    },

    handleAdd() {
      this.isEdit = false
      this.dialogTitle = 'Add Member'
      this.dialogVisible = true
      this.resetForm()
    },

    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = 'Edit Member'
      this.dialogVisible = true
      this.memberForm = {
        memberId: row.memberId,
        name: row.name,
        sex: row.sex,
        birthday: row.birthday,
        password: ''
      }
    },

    handleView(row) {
      this.currentMember = row
      this.viewDialogVisible = true
    },

    // delete member
    // not recoverable, confirm before proceeding
    async handleDelete(row) {
      this.$confirm(`Are you sure you want to delete member ${row.name}?`, 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteMember(row.memberId)
          this.$message.success('Deleted successfully')
          // refresh list
          this.fetchMemberList()
        } catch (error) {
          this.$message.error('Deletion failed')
          console.error('Failed to delete member:', error)
          // TODO better error message
        }
      }).catch(() => {
        // cancelled
        this.$message.info('Deletion canceled')
      })
    },

    // submit form
    submitForm() {
      this.$refs.memberForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            const payload = {
              name: this.memberForm.name,
              sex: this.memberForm.sex,
              birthday: this.memberForm.birthday
            }

            if (this.isEdit) {
              if (this.memberForm.password) {
                payload.password = this.memberForm.password
              }
              await updateMember(this.memberForm.memberId, payload)
              this.$message.success('Member updated successfully')
            } else {
              payload.password = this.memberForm.password
              if (this.memberForm.memberId) {
                payload.memberId = this.memberForm.memberId
              }
              await addMember(payload)
              this.$message.success('Member added successfully')
            }
            this.dialogVisible = false
            this.resetForm()
            // refresh data
            this.fetchMemberList()
          } catch (error) {
            // error message
            const errorMsg = this.isEdit ? 'Update failed' : 'Creation failed'
            this.$message.error(errorMsg)
            console.error('Submitting member form failed:', error)
            // debug
          } finally {
            this.submitting = false
          }
        } else {
          // validation failed
          console.log('Form validation failed')
        }
      })
    },

    resetForm() {
      this.memberForm = {
        memberId: '',
        name: '',
        sex: '男',
        birthday: '',
        password: ''
      }
      if (this.$refs.memberForm) {
        this.$refs.memberForm.clearValidate()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
  margin-bottom: 20px;
}

.operation-container {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.card-info {
  font-size: 12px;
  margin-bottom: 4px;
  color: #666;
}

.member-detail {
  .el-descriptions {
    margin-bottom: 20px;
  }
}
</style>