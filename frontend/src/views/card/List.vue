<template>
  <div class="app-container">
    <el-card class="table-container">
      <div class="operation-container">
        <el-button type="primary" icon="el-icon-plus" @click="handleRegister">Register Card</el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="handleReissue">Reissue Card</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="cardList"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column type="index" label="No." width="80" align="center" />
        <el-table-column prop="cardId" label="Card Number" width="240">
          <template slot="header">
            <i class="el-icon-credit-card" style="margin-right: 5px;"></i>Card Number
          </template>
          <template slot-scope="scope">
            <span class="card-id">{{ scope.row.cardId }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="memberId" label="Member ID" width="180">
          <template slot="header">
            <i class="el-icon-user" style="margin-right: 5px;"></i>Member ID
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="Balance (CNY)" width="120" align="right">
          <template slot="header">
            <i class="el-icon-money" style="margin-right: 5px;"></i>Balance (CNY)
          </template>
          <template slot-scope="scope">
            <span class="balance-value">{{ (scope.row.balance / 100).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="integral" label="Points" width="100" align="right">
          <template slot="header">
            <i class="el-icon-coin" style="margin-right: 5px;"></i>Points
          </template>
          <template slot-scope="scope">
            <span class="integral-value">{{ scope.row.integral }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Status" width="100" align="center">
          <template slot="header">
            <i class="el-icon-info" style="margin-right: 5px;"></i>Status
          </template>
          <template slot-scope="scope">
            <el-tag :type="scope.row.lose === 0 ? 'success' : 'danger'">
              <i :class="scope.row.lose === 0 ? 'el-icon-check' : 'el-icon-close'" style="margin-right: 3px;"></i>
              {{ scope.row.lose === 0 ? 'Active' : 'Lost' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="220" fixed="right">
          <template slot="header">
            <i class="el-icon-setting" style="margin-right: 5px;"></i>Actions
          </template>
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.lose === 0"
              type="text"
              size="small"
              icon="el-icon-warning"
              @click="handleLose(scope.row)"
            >
              Report Lost
            </el-button>
            <el-button
              v-if="scope.row.lose === 1"
              type="text"
              size="small"
              icon="el-icon-success"
              @click="handleCancel(scope.row)"
            >
              Restore Card
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-money"
              @click="handleRecharge(scope.row)"
            >
              Recharge
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        background
        :current-page="pagination.pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- register member card dialog -->
    <el-dialog title="Register Membership Card" :visible.sync="registerDialogVisible" width="400px">
      <el-form ref="registerForm" :model="registerForm" :rules="registerRules" label-width="100px">
        <el-form-item label="Member ID" prop="memberId">
          <el-input v-model="registerForm.memberId" placeholder="Enter member ID" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="registerDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitRegister" :loading="submitting">Confirm</el-button>
      </div>
    </el-dialog>

    <!-- reissue card dialog -->
    <el-dialog title="Reissue Card" :visible.sync="reissueDialogVisible" width="400px">
      <el-form ref="reissueForm" :model="reissueForm" :rules="reissueRules" label-width="100px">
        <el-form-item label="Original Card Number" prop="cardId">
          <el-input v-model="reissueForm.cardId" placeholder="Enter the card number to reissue" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="reissueDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitReissue" :loading="submitting">Confirm</el-button>
      </div>
    </el-dialog>

    <!-- recharge dialog -->
    <el-dialog title="Card Recharge" :visible.sync="rechargeDialogVisible" width="400px">
      <el-form ref="rechargeForm" :model="rechargeForm" :rules="rechargeRules" label-width="100px">
        <el-form-item label="Card Number">
          <span>{{ currentCard.cardId }}</span>
        </el-form-item>
        <el-form-item label="Current Balance">
          <span>{{ (currentCard.balance / 100).toFixed(2) }} CNY</span>
        </el-form-item>
        <el-form-item label="Recharge Amount" prop="value">
          <el-input-number
            v-model="rechargeForm.value"
            :min="1"
            :max="10000"
            placeholder="Enter recharge amount"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="rechargeDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitRecharge" :loading="submitting">Confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCardList, registerCard, loseCard, cancelCard, reissueCard, rechargeCard } from '@/api/member'

export default {
  name: 'CardList',
  data() {
    return {
      loading: false,
      submitting: false,
      cardList: [],
      pagination: {
        pageIndex: 1,
        pageSize: 10,
        pageTotal: 0
      },
      registerDialogVisible: false,
      reissueDialogVisible: false,
      rechargeDialogVisible: false,
      currentCard: {},
      registerForm: {
        memberId: ''
      },
      reissueForm: {
        cardId: ''
      },
      rechargeForm: {
        value: 100
      },
      registerRules: {
        memberId: [
          { required: true, message: 'Please enter the member ID', trigger: 'blur' }
        ]
      },
      reissueRules: {
        cardId: [
          { required: true, message: 'Please enter the card number', trigger: 'blur' }
        ]
      },
      rechargeRules: {
        value: [
          { required: true, message: 'Please enter a recharge amount', trigger: 'blur' },
          { type: 'number', min: 1, message: 'Recharge amount must be greater than 0', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.fetchCardList()
  },
  methods: {
    async fetchCardList() {
      this.loading = true
      try {
        const response = await getCardList({
          pageIndex: this.pagination.pageIndex,
          pageSize: this.pagination.pageSize
        })
        this.cardList = response.data ? response.data.list : []
        this.pagination.pageTotal = response.data ? response.data.pageTotal : 0
      } catch (error) {
        this.$message.error('Failed to load membership cards')
        console.error('Failed to load membership cards:', error)
      } finally {
        this.loading = false
      }
    },

    handleRegister() {
      this.registerDialogVisible = true
      this.registerForm = { memberId: '' }
    },

    async submitRegister() {
      this.$refs.registerForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            await registerCard(this.registerForm.memberId)
            this.$message.success('Card registered successfully')
            this.registerDialogVisible = false
            this.fetchCardList()
          } catch (error) {
            this.$message.error('Failed to register card')
          } finally {
            this.submitting = false
          }
        }
      })
    },

    handleReissue() {
      this.reissueDialogVisible = true
      this.reissueForm = { cardId: '' }
    },

    async submitReissue() {
      this.$refs.reissueForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            const response = await reissueCard(this.reissueForm.cardId)
            const newCardId = response?.data
            this.$message.success(newCardId ? `Card reissued, new card ID: ${newCardId}` : 'Card reissued successfully')
            this.reissueDialogVisible = false
            this.fetchCardList()
          } catch (error) {
            this.$message.error('Failed to reissue card')
          } finally {
            this.submitting = false
          }
        }
      })
    },

    handleLose(row) {
      this.$confirm(`Are you sure you want to report card ${row.cardId} as lost?`, 'Confirmation', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async () => {
        try {
          await loseCard(row.cardId)
          this.$message.success('Card reported lost')
          this.fetchCardList()
        } catch (error) {
          this.$message.error('Failed to report card lost')
        }
      })
    },

    handleCancel(row) {
      this.$confirm(`Reactivate card ${row.cardId}?`, 'Confirmation', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async () => {
        try {
          await cancelCard(row.cardId)
          this.$message.success('Card reactivated successfully')
          this.fetchCardList()
        } catch (error) {
          this.$message.error('Failed to reactivate card')
        }
      })
    },

    handleRecharge(row) {
      this.currentCard = row
      this.rechargeForm = { value: 100 }
      this.rechargeDialogVisible = true
    },

    async submitRecharge() {
      this.$refs.rechargeForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            await rechargeCard(this.currentCard.cardId, this.rechargeForm.value * 100)
            this.$message.success('Recharge successful')
            this.rechargeDialogVisible = false
            this.fetchCardList()
          } catch (error) {
            this.$message.error('Recharge failed')
          } finally {
            this.submitting = false
          }
        }
      })
    },

    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.pageIndex = 1
      this.fetchCardList()
    },

    handleCurrentChange(val) {
      this.pagination.pageIndex = val
      this.fetchCardList()
    }
  }
}
</script>

<style lang="scss" scoped>
.operation-container {
  margin-bottom: 20px;
}

.card-id {
  font-family: 'Courier New', monospace;
  color: #606266;
  font-size: 13px;
  background: #f5f7fa;
  padding: 2px 6px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.balance-value {
  color: #E6A23C;
  font-weight: bold;
}

.integral-value {
  color: #67C23A;
  font-weight: bold;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>