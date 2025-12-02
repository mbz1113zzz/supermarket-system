<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- points adjustment form -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="adjust-card">
          <div slot="header" class="clearfix">
            <span>Manual Points Adjustment</span>
          </div>

          <el-form ref="adjustForm" :model="adjustForm" :rules="adjustRules" label-width="120px" size="small">
            <el-form-item label="Member Card Number" prop="cardId">
              <el-input
                v-model="adjustForm.cardId"
                placeholder="Enter a member card number"
                style="width: 100%"
                @blur="handleCardIdBlur"
              >
                <el-button slot="append" icon="el-icon-search" @click="handleSearchCard"></el-button>
              </el-input>
              <div v-if="memberInfo" class="member-info">
                <el-tag type="success">{{ memberInfo.name }} - Current Points: {{ memberInfo.integral }}</el-tag>
              </div>
            </el-form-item>

            <el-form-item label="Adjustment Type" prop="adjustType">
              <el-radio-group v-model="adjustForm.adjustType" @change="handleTypeChange">
                <el-radio label="increase">
                  <i class="el-icon-plus"></i> Add Points
                </el-radio>
                <el-radio label="decrease">
                  <i class="el-icon-minus"></i> Deduct Points
                </el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="Points Amount" prop="integral">
              <el-input-number
                v-model="adjustForm.integral"
                :min="1"
                :max="999999"
                placeholder="Enter the points amount"
                style="width: 100%"
              />
              <div class="form-tip">
                <span v-if="adjustForm.adjustType === 'increase'" class="tip-positive">
                  Will add {{ adjustForm.integral }} points
                </span>
                <span v-else class="tip-negative">
                  Will deduct {{ adjustForm.integral }} points
                </span>
              </div>
            </el-form-item>

            <el-form-item label="Adjustment Reason" prop="reason">
              <el-select v-model="adjustForm.reason" placeholder="Select a reason" style="width: 100%">
                <el-option label="Birthday Reward" value="birthday"></el-option>
                <el-option label="Purchase Rebate" value="rebate"></el-option>
                <el-option label="Campaign Reward" value="activity"></el-option>
                <el-option label="Complaint Compensation" value="compensation"></el-option>
                <el-option label="System Adjustment" value="system"></el-option>
                <el-option label="Manual Adjustment" value="manual"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="Remarks" prop="remark">
              <el-input
                v-model="adjustForm.remark"
                type="textarea"
                :rows="3"
                placeholder="Enter an optional remark"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleSubmitAdjust" :loading="submitting" :disabled="!memberInfo">
                Submit Adjustment
              </el-button>
              <el-button @click="resetForm">Reset</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- points adjustment records -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="history-card">
          <div slot="header" class="clearfix">
            <span>Latest Adjustments</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="fetchAdjustHistory">Refresh</el-button>
          </div>

          <el-table
            v-loading="historyLoading"
            :data="adjustHistory"
            size="small"
            style="width: 100%"
            max-height="600"
          >
            <el-table-column prop="createTime" label="Time" width="150">
              <template slot="header">
                <i class="el-icon-time" style="margin-right: 5px;"></i>Time
              </template>
            </el-table-column>
            <el-table-column prop="cardId" label="Card Number" width="240">
              <template slot="header">
                <i class="el-icon-credit-card" style="margin-right: 5px;"></i>Card Number
              </template>
              <template slot-scope="scope">
                <span class="card-id">{{ scope.row.cardId }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="memberName" label="Member Name" width="100">
              <template slot="header">
                <i class="el-icon-user" style="margin-right: 5px;"></i>Member Name
              </template>
            </el-table-column>
            <el-table-column label="Adjustment Type" width="100">
              <template slot="header">
                <i class="el-icon-info" style="margin-right: 5px;"></i>Adjustment Type
              </template>
              <template slot-scope="scope">
                <el-tag :type="scope.row.adjustType === 'increase' ? 'success' : 'danger'" size="mini">
                  <i :class="scope.row.adjustType === 'increase' ? 'el-icon-plus' : 'el-icon-minus'" style="margin-right: 3px;"></i>
                  {{ scope.row.adjustType === 'increase' ? 'Add' : 'Deduct' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="integral" label="Points" width="80" align="right">
              <template slot="header">
                <i class="el-icon-coin" style="margin-right: 5px;"></i>Points
              </template>
              <template slot-scope="scope">
                <span :class="scope.row.adjustType === 'increase' ? 'integral-positive' : 'integral-negative'">
                  {{ scope.row.adjustType === 'increase' ? '+' : '-' }}{{ scope.row.integral }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="reason" label="Reason" min-width="100">
              <template slot="header">
                <i class="el-icon-question" style="margin-right: 5px;"></i>Reason
              </template>
              <template slot-scope="scope">
                <span>{{ getReasonText(scope.row.reason) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="operator" label="Operator" width="100">
              <template slot="header">
                <i class="el-icon-user-solid" style="margin-right: 5px;"></i>Operator
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- points overview stats -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>Points Overview</span>
          </div>

          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stats-item">
                <div class="stats-number">{{ totalIssued }}</div>
                <div class="stats-label">Points Issued Today</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stats-item">
                <div class="stats-number">{{ totalDeducted }}</div>
                <div class="stats-label">Points Deducted Today</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stats-item">
                <div class="stats-number">{{ netIncrease }}</div>
                <div class="stats-label">Net Points Increase Today</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stats-item">
                <div class="stats-number">{{ adjustCount }}</div>
                <div class="stats-label">Adjustments Today</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCardList, adjustPoints, getAllTransactionRecords } from '@/api/member'

export default {
  name: 'PointsAdjust',
  data() {
    return {
      submitting: false,
      historyLoading: false,
      memberInfo: null,
      adjustForm: {
        cardId: '',
        adjustType: 'increase',
        integral: 100,
        reason: '',
        remark: ''
      },
      adjustRules: {
        cardId: [
          { required: true, message: 'Please enter a member card number', trigger: 'blur' }
        ],
        adjustType: [
          { required: true, message: 'Please select an adjustment type', trigger: 'change' }
        ],
        integral: [
          { required: true, message: 'Please enter the points amount', trigger: 'blur' },
          { type: 'number', min: 1, message: 'Points must be greater than 0', trigger: 'blur' }
        ],
        reason: [
          { required: true, message: 'Please select an adjustment reason', trigger: 'change' }
        ]
      },
      adjustHistory: [],
      totalIssued: 0,
      totalDeducted: 0,
      netIncrease: 0,
      adjustCount: 0
    }
  },
  created() {
    this.fetchAdjustHistory()
  },
  methods: {
    async handleCardIdBlur() {
      if (this.adjustForm.cardId) {
        await this.searchMember(this.adjustForm.cardId)
      }
    },

    async handleSearchCard() {
      if (!this.adjustForm.cardId) {
        this.$message.warning('Please enter a member card number first')
        return
      }
      await this.searchMember(this.adjustForm.cardId)
    },

    async searchMember(cardId) {
      try {
        // call real API to query member card info
        const response = await getCardList({
          pageIndex: 1,
          pageSize: 1,
          memberId: cardId.startsWith('C') ? '' : cardId // if it's a card number, don't filter by memberId
        })

        const primaryList = response?.data?.list || []
        if (primaryList.length > 0) {
          const card = primaryList[0]
          // find the corresponding member info
          this.memberInfo = {
            cardId: card.cardId,
            name: `Member ${card.memberId}`, // backend might not return name, using memberId
            integral: card.integral,
            memberId: card.memberId,
            balance: card.balance,
            lose: card.lose
          }
        } else {
          // try directly querying card number
          const memberResponse = await getCardList({
            pageIndex: 1,
            pageSize: 10
          })

          const fallbackList = memberResponse?.data?.list || []
          if (fallbackList.length > 0) {
            const foundCard = fallbackList.find(card => card.cardId === cardId)
            if (foundCard) {
              this.memberInfo = {
                cardId: foundCard.cardId,
                name: `Member ${foundCard.memberId}`,
                integral: foundCard.integral,
                memberId: foundCard.memberId,
                balance: foundCard.balance,
                lose: foundCard.lose
              }
            } else {
              this.memberInfo = null
              this.$message.error('No information found for this card number')
            }
          } else {
            this.memberInfo = null
            this.$message.error('No information found for this card number')
          }
        }
      } catch (error) {
        this.memberInfo = null
        this.$message.error('Failed to query member information')
        console.error('Failed to query member information:', error)
      }
    },

    handleTypeChange() {
      // clear remark when type changes
      this.adjustForm.remark = ''
    },

    async handleSubmitAdjust() {
      if (!this.memberInfo) {
        this.$message.error('Please query the member information first')
        return
      }

      // if deducting points, check points balance
      if (this.adjustForm.adjustType === 'decrease' && this.adjustForm.integral > this.memberInfo.integral) {
        this.$message.error('Deducted points cannot exceed the current balance')
        return
      }

      this.$refs.adjustForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            // call real points adjustment API
            // note: backend's exchangeIntegral only deducts points, adding points needs other methods
            const integral = this.adjustForm.integral
            let apiResponse

            if (this.adjustForm.adjustType === 'decrease') {
              // use exchangeIntegral to deduct points
              apiResponse = await adjustPoints(this.memberInfo.memberId, integral)
            } else {
              // adding points uses mock data for now, as backend doesn't provide an add points API
              // might need to implement via purchase rebates or other methods
              this.$message.warning('Adding points is not supported yet. Please contact the administrator')
              return
            }

            if (apiResponse) {
              const action = this.adjustForm.adjustType === 'increase' ? 'added' : 'deducted'
              this.$message.success(`Points ${action} successfully`)

              // update stats info
              this.updateStats()

              // re-query member info to get latest points
              await this.searchMember(this.adjustForm.cardId)

              // refresh history
              this.fetchAdjustHistory()

              // reset form
              this.resetForm()
            }
          } catch (error) {
            this.$message.error('Points adjustment failed')
            console.error('Points adjustment failed:', error)
          } finally {
            this.submitting = false
          }
        }
      })
    },

    resetForm() {
      this.adjustForm = {
        cardId: '',
        adjustType: 'increase',
        integral: 100,
        reason: '',
        remark: ''
      }
      this.memberInfo = null
      if (this.$refs.adjustForm) {
        this.$refs.adjustForm.clearValidate()
      }
    },

    async fetchAdjustHistory() {
      this.historyLoading = true
      try {
        // call real API to get transaction records
        const response = await getAllTransactionRecords(1, 50)

        const list = response?.data?.list || []
        if (list.length > 0) {
          // convert data format to fit frontend display
          this.adjustHistory = list.map(record => ({
            id: record.id,
            createTime: this.formatDateTime(record.time),
            cardId: record.cardId,
            memberName: `Member ${record.cardId?.substring(0, 8) || ''}`,
            adjustType: record.spendType === 1 ? (record.value > 0 ? 'increase' : 'decrease') : 'decrease',
            integral: Math.abs(record.value || 0),
            reason: this.getReasonFromDescription(record.description),
            operator: 'Admin'
          }));

          // calculate stats
          this.calculateStatistics()
        }
      } catch (error) {
        this.$message.error('Failed to fetch adjustment records')
        console.error('Failed to fetch adjustment records:', error)
      } finally {
        this.historyLoading = false
      }
    },

    formatDateTime(dateStr) {
      if (!dateStr) return '-'
      const date = new Date(dateStr)
      return date.toLocaleString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      }).replace(/\//g, '-')
    },

    getReasonFromDescription(description) {
      if (!description) return 'other'

      const desc = description.toLowerCase()
      if (desc.includes('生日') || desc.includes('birthday')) return 'birthday'
      if (desc.includes('消费') || desc.includes('purchase')) return 'rebate'
      if (desc.includes('活动') || desc.includes('activity')) return 'activity'
      if (desc.includes('补偿') || desc.includes('compensation')) return 'compensation'
      if (desc.includes('系统') || desc.includes('system')) return 'system'
      if (desc.includes('兑换') || desc.includes('exchange')) return 'manual'

      return 'other'
    },

    getReasonText(reason) {
      const reasonMap = {
        'birthday': 'Birthday Reward',
        'rebate': 'Purchase Rebate',
        'activity': 'Campaign Reward',
        'compensation': 'Complaint Compensation',
        'system': 'System Adjustment',
        'manual': 'Manual Adjustment',
        'other': 'Other'
      }
      return reasonMap[reason] || 'Other'
    },

    calculateStatistics() {
      let issued = 0
      let deducted = 0
      let count = 0

      // only count today's records
      const today = new Date()
      today.setHours(0, 0, 0, 0)

      this.adjustHistory.forEach(record => {
        try {
          const recordDate = new Date(record.createTime)

          if (recordDate >= today) {
            count++
            if (record.adjustType === 'increase') {
              issued += record.integral
            } else {
              deducted += record.integral
            }
          }
        } catch (e) {
          // ignore date parsing errors
        }
      })

      this.totalIssued = issued
      this.totalDeducted = deducted
      this.netIncrease = issued - deducted
      this.adjustCount = count
    },

    updateStats() {
      const integral = this.adjustForm.integral
      if (this.adjustForm.adjustType === 'increase') {
        this.totalIssued += integral
        this.netIncrease += integral
      } else {
        this.totalDeducted += integral
        this.netIncrease -= integral
      }
      this.adjustCount += 1
    }
  }
}
</script>

<style lang="scss" scoped>
.adjust-card, .history-card {
  min-height: 600px;
}

.member-info {
  margin-top: 8px;
  font-size: 14px;
}

.form-tip {
  margin-top: 5px;
  font-size: 12px;

  .tip-positive {
    color: #67C23A;
  }

  .tip-negative {
    color: #F56C6C;
  }
}

.integral-positive {
  color: #67C23A;
  font-weight: bold;
}

.integral-negative {
  color: #F56C6C;
  font-weight: bold;
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