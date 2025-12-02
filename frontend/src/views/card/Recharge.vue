<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- quick recharge -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="recharge-card">
          <div slot="header" class="clearfix">
            <span>Quick Recharge</span>
          </div>

          <el-form ref="quickRechargeForm" :model="quickRechargeForm" :rules="rechargeRules" label-width="120px" size="small">
            <el-form-item label="Card Number" prop="cardId">
              <el-input
                v-model="quickRechargeForm.cardId"
                placeholder="Enter the card number"
                @blur="handleCardIdBlur"
              >
                <el-button slot="append" icon="el-icon-search" @click="handleSearchCard"></el-button>
              </el-input>
              <div v-if="cardInfo" class="card-info">
                <el-tag type="success">
                  <i class="el-icon-user" style="margin-right: 3px;"></i>
                  {{ cardInfo.memberId }} - Current balance: {{ (cardInfo.balance / 100).toFixed(2) }} CNY
                </el-tag>
              </div>
            </el-form-item>

            <el-form-item label="Recharge Amount" prop="amount">
              <el-input-number
                v-model="quickRechargeForm.amount"
                :min="1"
                :max="10000"
                placeholder="Enter recharge amount"
                style="width: 100%"
              />
            </el-form-item>

            <el-form-item label="Quick Amount">
              <el-button-group>
                <el-button size="small" icon="el-icon-coin" @click="quickRechargeForm.amount = 50">50 CNY</el-button>
                <el-button size="small" icon="el-icon-coin" @click="quickRechargeForm.amount = 100">100 CNY</el-button>
                <el-button size="small" icon="el-icon-coin" @click="quickRechargeForm.amount = 200">200 CNY</el-button>
                <el-button size="small" icon="el-icon-coin" @click="quickRechargeForm.amount = 500">500 CNY</el-button>
              </el-button-group>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="submitQuickRecharge" :loading="submitting" :disabled="!cardInfo">
                Confirm Recharge
              </el-button>
              <el-button @click="resetQuickForm">Reset</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- batch recharge -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="batch-recharge-card">
          <div slot="header" class="clearfix">
            <span>Batch Recharge</span>
          </div>

          <el-form ref="batchRechargeForm" :model="batchRechargeForm" :rules="batchRules" label-width="120px" size="small">
            <el-form-item label="Card Numbers" prop="cardIds">
              <el-input
                v-model="batchRechargeForm.cardIds"
                type="textarea"
                :rows="4"
                placeholder="Enter card numbers, separated by commas or new lines"
              />
              <div class="form-tip">Supports batch recharge. Separate multiple card numbers with commas or new lines.</div>
            </el-form-item>

            <el-form-item label="Recharge Amount" prop="amount">
              <el-input-number
                v-model="batchRechargeForm.amount"
                :min="1"
                :max="10000"
                placeholder="Enter recharge amount"
                style="width: 100%"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="submitBatchRecharge" :loading="batchSubmitting">
                Batch Recharge
              </el-button>
              <el-button @click="resetBatchForm">Reset</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- recharge records -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>Recent Recharge Records</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="fetchRechargeHistory">Refresh</el-button>
          </div>

          <el-table
            v-loading="historyLoading"
            :data="rechargeHistory"
            size="small"
            style="width: 100%"
          >
            <el-table-column prop="time" label="Recharge Time" width="150">
              <template slot="header">
                <i class="el-icon-time" style="margin-right: 5px;"></i>Recharge Time
              </template>
            </el-table-column>
            <el-table-column prop="cardId" label="Card Number" width="240">
              <template slot="header">
                <i class="el-icon-credit-card" style="margin-right: 5px;"></i>Card Number
              </template>
            </el-table-column>
            <el-table-column prop="memberId" label="Member ID" width="180">
              <template slot="header">
                <i class="el-icon-user" style="margin-right: 5px;"></i>Member ID
              </template>
            </el-table-column>
            <el-table-column prop="amount" label="Recharge Amount (CNY)" width="120" align="right">
              <template slot="header">
                <i class="el-icon-money" style="margin-right: 5px;"></i>Recharge Amount (CNY)
              </template>
              <template slot-scope="scope">
                <span class="recharge-amount">{{ (scope.row.value / 100).toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="balanceAfter" label="Balance After (CNY)" width="120" align="right">
              <template slot="header">
                <i class="el-icon-wallet" style="margin-right: 5px;"></i>Balance After (CNY)
              </template>
              <template slot-scope="scope">
                <span class="balance-after">{{ scope.row.balanceAfter ? (scope.row.balanceAfter / 100).toFixed(2) : '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="Remarks" min-width="150">
              <template slot="header">
                <i class="el-icon-document" style="margin-right: 5px;"></i>Remarks
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- recharge statistics -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>Recharge Statistics</span>
          </div>

          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stats-item">
                <div class="stats-number">{{ todayRecharge }}</div>
                <div class="stats-label">Today's Recharge Amount (CNY)</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stats-item">
                <div class="stats-number">{{ todayRechargeCount }}</div>
                <div class="stats-label">Today's Recharge Count</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stats-item">
                <div class="stats-number">{{ monthRecharge }}</div>
                <div class="stats-label">Monthly Recharge Amount (CNY)</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stats-item">
                <div class="stats-number">{{ monthRechargeCount }}</div>
                <div class="stats-label">Monthly Recharge Count</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCardList, rechargeCard } from '@/api/member'

export default {
  name: 'CardRecharge',
  data() {
    return {
      submitting: false,
      batchSubmitting: false,
      historyLoading: false,
      cardInfo: null,
      quickRechargeForm: {
        cardId: '',
        amount: 100
      },
      batchRechargeForm: {
        cardIds: '',
        amount: 100
      },
      rechargeRules: {
        cardId: [
          { required: true, message: 'Please enter the card number', trigger: 'blur' }
        ],
        amount: [
          { required: true, message: 'Please enter a recharge amount', trigger: 'blur' },
          { type: 'number', min: 1, message: 'Recharge amount must be greater than 0', trigger: 'blur' }
        ]
      },
      batchRules: {
        cardIds: [
          { required: true, message: 'Please enter card numbers', trigger: 'blur' }
        ],
        amount: [
          { required: true, message: 'Please enter a recharge amount', trigger: 'blur' },
          { type: 'number', min: 1, message: 'Recharge amount must be greater than 0', trigger: 'blur' }
        ]
      },
      rechargeHistory: [
        {
          id: 1,
          time: '2024-01-15 14:30',
          cardId: 'f226a06fed11438882f0d3cffd0ece4f',
          memberId: '567ced95cf1541bc94ccb3dfa767b53f',
          value: 10000,
          balanceAfter: 11800,
          description: 'Card recharge'
        },
        {
          id: 2,
          time: '2024-01-15 13:45',
          cardId: '8b06e3dcd4334c368d666081f666b500',
          memberId: '567ced95cf1541bc94ccb3dfa767b53f',
          value: 5000,
          balanceAfter: 7551,
          description: 'Card recharge'
        },
        {
          id: 3,
          time: '2024-01-15 12:20',
          cardId: 'ba0977e470ba4f6fb48b8c157da98b4d',
          memberId: 'edc5c2070d154561b8d5aa94ded0ccd9',
          value: 20000,
          balanceAfter: 29900,
          description: 'Card recharge'
        }
      ],
      todayRecharge: 35000,
      todayRechargeCount: 3,
      monthRecharge: 156000,
      monthRechargeCount: 12
    }
  },
  created() {
    this.fetchRechargeHistory()
  },
  methods: {
    async handleCardIdBlur() {
      if (this.quickRechargeForm.cardId) {
        await this.searchCard(this.quickRechargeForm.cardId)
      }
    },

    async handleSearchCard() {
      if (!this.quickRechargeForm.cardId) {
        this.$message.warning('Please enter the card number first')
        return
      }
      await this.searchCard(this.quickRechargeForm.cardId)
    },

    async searchCard(cardId) {
      try {
        const response = await getCardList({
          pageIndex: 1,
          pageSize: 10
        })

        const list = response?.data?.list || []
        if (list.length > 0) {
          const foundCard = list.find(card => card.cardId === cardId)
          if (foundCard) {
            this.cardInfo = foundCard
          } else {
            this.cardInfo = null
            this.$message.error('Card number not found')
          }
        } else {
          this.cardInfo = null
          this.$message.error('Failed to retrieve card information')
        }
      } catch (error) {
        this.cardInfo = null
        this.$message.error('Failed to retrieve card information')
        console.error('Failed to retrieve card information:', error)
      }
    },

    async submitQuickRecharge() {
      if (!this.cardInfo) {
        this.$message.error('Please search for the card information first')
        return
      }

      this.$refs.quickRechargeForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            await rechargeCard(this.cardInfo.cardId, this.quickRechargeForm.amount * 100)
            this.$message.success('Recharge successful')

            // update stats
            this.updateStats(this.quickRechargeForm.amount)

            // refresh card info
            await this.searchCard(this.quickRechargeForm.cardId)

            // refresh history
            this.fetchRechargeHistory()

            // reset form
            this.resetQuickForm()
          } catch (error) {
            this.$message.error('Recharge failed')
            console.error('Recharge failed:', error)
          } finally {
            this.submitting = false
          }
        }
      })
    },

    async submitBatchRecharge() {
      this.$refs.batchRechargeForm.validate(async (valid) => {
        if (valid) {
          this.batchSubmitting = true
          try {
            const cardIds = this.batchRechargeForm.cardIds
              .split(/[,，\n]/)
              .map(id => id.trim())
              .filter(id => id)

            if (cardIds.length === 0) {
              this.$message.error('Please enter valid card numbers')
              return
            }

            let successCount = 0
            let failCount = 0

            for (const cardId of cardIds) {
              try {
                await rechargeCard(cardId, this.batchRechargeForm.amount * 100)
                successCount++
              } catch (error) {
                failCount++
                console.error(`Recharge failed for card ${cardId}:`, error)
              }
            }

            this.$message.success(`Batch recharge completed! Successes: ${successCount}, Failures: ${failCount}`)

            // update stats
            this.updateStats(this.batchRechargeForm.amount * successCount)

            // refresh history
            this.fetchRechargeHistory()

            // reset form
            this.resetBatchForm()
          } catch (error) {
            this.$message.error('Batch recharge failed')
          } finally {
            this.batchSubmitting = false
          }
        }
      })
    },

    resetQuickForm() {
      this.quickRechargeForm = {
        cardId: '',
        amount: 100
      }
      this.cardInfo = null
      if (this.$refs.quickRechargeForm) {
        this.$refs.quickRechargeForm.clearValidate()
      }
    },

    resetBatchForm() {
      this.batchRechargeForm = {
        cardIds: '',
        amount: 100
      }
      if (this.$refs.batchRechargeForm) {
        this.$refs.batchRechargeForm.clearValidate()
      }
    },

    async fetchRechargeHistory() {
      this.historyLoading = true
      try {
        // call actual API to get recharge history
        // const response = await rechargeApi.getRechargeHistory()
        // this.rechargeHistory = response.data

        // simulate API call delay
        await new Promise(resolve => setTimeout(resolve, 300))
      } catch (error) {
        this.$message.error('Failed to retrieve recharge records')
        console.error('Failed to retrieve recharge records:', error)
      } finally {
        this.historyLoading = false
      }
    },

    updateStats(amount) {
      this.todayRecharge += amount
      this.todayRechargeCount += 1
      this.monthRecharge += amount
      this.monthRechargeCount += 1
    }
  }
}
</script>

<style lang="scss" scoped>
.recharge-card, .batch-recharge-card {
  min-height: 400px;
}

.card-info {
  margin-top: 8px;
  font-size: 14px;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.recharge-amount {
  color: #67C23A;
  font-weight: bold;
}

.balance-after {
  color: #409EFF;
  font-weight: bold;
}

.stats-item {
  text-align: center;
  padding: 20px 0;

  .stats-number {
    font-size: 32px;
    font-weight: bold;
    color: #409EFF;
    margin-bottom: 8px;
  }

  .stats-label {
    font-size: 14px;
    color: #909399;
  }
}
</style>