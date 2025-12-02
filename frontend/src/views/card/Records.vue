<template>
  <div class="app-container">
    <el-card>
      <div class="filter-container">
        <el-form :inline="true" :model="queryParams" size="small">
          <el-form-item label="Card Number">
            <el-input
              v-model="queryParams.cardId"
              placeholder="Enter card number"
              clearable
              style="width: 200px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="Member ID">
            <el-input
              v-model="queryParams.memberId"
              placeholder="Enter member ID"
              clearable
              style="width: 180px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="Transaction Type">
            <el-select v-model="queryParams.spendtype" placeholder="Select transaction type" clearable style="width: 120px">
              <el-option label="Cash" value="0" />
              <el-option label="Points" value="1" />
            </el-select>
          </el-form-item>
          <el-form-item label="Date Range">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="to"
              start-placeholder="Start date"
              end-placeholder="End date"
              value-format="yyyy-MM-dd"
              style="width: 350px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">Search</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">Reset</el-button>
            <el-button type="success" icon="el-icon-download" @click="handleExport">Export</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        v-loading="loading"
        :data="recordList"
        border
        style="width: 100%"
        size="small"
      >
        <el-table-column type="index" label="No." width="80" align="center" />
        <el-table-column prop="time" label="Transaction Time" width="160">
          <template slot="header">
            <i class="el-icon-time" style="margin-right: 5px;"></i>Transaction Time
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
        <el-table-column prop="memberId" label="Member ID" width="180">
          <template slot="header">
            <i class="el-icon-user" style="margin-right: 5px;"></i>Member ID
          </template>
        </el-table-column>
        <el-table-column label="Transaction Type" width="100" align="center">
          <template slot="header">
            <i class="el-icon-info" style="margin-right: 5px;"></i>Transaction Type
          </template>
          <template slot-scope="scope">
            <el-tag :type="scope.row.spendtype === 0 ? 'primary' : 'success'" size="mini">
              <i :class="scope.row.spendtype === 0 ? 'el-icon-money' : 'el-icon-coin'" style="margin-right: 3px;"></i>
              {{ scope.row.spendtype === 0 ? 'Cash' : 'Points' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Transaction Amount" width="120" align="right">
          <template slot="header">
            <i class="el-icon-wallet" style="margin-right: 5px;"></i>Transaction Amount
          </template>
          <template slot-scope="scope">
            <span v-if="scope.row.spendtype === 0" :class="scope.row.value > 0 ? 'amount-positive' : 'amount-negative'">
              {{ scope.row.value > 0 ? '+' : '' }}{{ (scope.row.value / 100).toFixed(2) }} CNY
            </span>
            <span v-else :class="scope.row.value > 0 ? 'integral-positive' : 'integral-negative'">
              {{ scope.row.value > 0 ? '+' : '' }}{{ scope.row.value }} points
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="Transaction Description" min-width="150">
          <template slot="header">
            <i class="el-icon-document" style="margin-right: 5px;"></i>Transaction Description
          </template>
        </el-table-column>
        <el-table-column prop="orderid" label="Order ID" width="180">
          <template slot="header">
            <i class="el-icon-s-order" style="margin-right: 5px;"></i>Order ID
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="100" fixed="right">
          <template slot="header">
            <i class="el-icon-setting" style="margin-right: 5px;"></i>Actions
          </template>
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >
              Details
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

    <!-- details dialog -->
    <el-dialog title="Transaction Details" :visible.sync="detailDialogVisible" width="600px">
      <div v-if="currentRecord" class="record-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Transaction Time">{{ currentRecord.time }}</el-descriptions-item>
          <el-descriptions-item label="Transaction Type">{{ currentRecord.spendtype === 0 ? 'Cash' : 'Points' }}</el-descriptions-item>
          <el-descriptions-item label="Card Number">{{ currentRecord.cardId }}</el-descriptions-item>
          <el-descriptions-item label="Member ID">{{ currentRecord.memberId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Transaction Amount">
            <span v-if="currentRecord.spendtype === 0" :class="currentRecord.value > 0 ? 'amount-positive' : 'amount-negative'">
              {{ currentRecord.value > 0 ? '+' : '' }}{{ (currentRecord.value / 100).toFixed(2) }} CNY
            </span>
            <span v-else :class="currentRecord.value > 0 ? 'integral-positive' : 'integral-negative'">
              {{ currentRecord.value > 0 ? '+' : '' }}{{ currentRecord.value }} points
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="Order ID">{{ currentRecord.orderid || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Transaction Description" :span="2">{{ currentRecord.description || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">Close</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCardList } from '@/api/member'

export default {
  name: 'CardRecords',
  data() {
    return {
      loading: false,
      recordList: [
        {
          id: 1,
          time: '2024-01-15 14:30:00',
          cardId: 'f226a06fed11438882f0d3cffd0ece4f',
          memberId: '567ced95cf1541bc94ccb3dfa767b53f',
          value: 10000,
          spendtype: 0,
          orderid: null,
          description: 'Card recharge'
        },
        {
          id: 2,
          time: '2024-01-15 16:45:30',
          cardId: '8b06e3dcd4334c368d666081f666b500',
          memberId: '567ced95cf1541bc94ccb3dfa767b53f',
          value: -1700,
          spendtype: 0,
          orderid: '20200430002',
          description: 'Product purchase order'
        },
        {
          id: 3,
          time: '2024-01-15 17:40:48',
          cardId: 'ba0977e470ba4f6fb48b8c157da98b4d',
          memberId: 'edc5c2070d154561b8d5aa94ded0ccd9',
          value: -990,
          spendtype: 0,
          orderid: '20200430003',
          description: 'Product purchase order'
        },
        {
          id: 4,
          time: '2024-01-15 18:20:34',
          cardId: '09961c29e8494649b29851388ef440fb',
          memberId: '569b67f5c340462f9c343a98ab6e64db',
          value: -1990,
          spendtype: 0,
          orderid: '20200501001',
          description: 'Product purchase order'
        },
        {
          id: 5,
          time: '2024-01-15 19:30:51',
          cardId: '8c0328f28b60400f983a806d67f705cc',
          memberId: '288bced48b0243bdaefb4d602fa30c62',
          value: -16066,
          spendtype: 1,
          orderid: '20200611001',
          description: 'Points payment order'
        }
      ],
      queryParams: {
        cardId: '',
        memberId: '',
        spendtype: ''
      },
      dateRange: [],
      pagination: {
        pageIndex: 1,
        pageSize: 10,
        pageTotal: 0
      },
      detailDialogVisible: false,
      currentRecord: null
    }
  },
  created() {
    this.fetchRecordList()
  },
  methods: {
    async fetchRecordList() {
      this.loading = true
      try {
        // call actual API to get transaction records
        // const response = await recordApi.getRecordList(this.queryParams)
        // this.recordList = response.data.list
        // this.pagination.pageTotal = response.data.pageTotal

        // simulate API call delay
        await new Promise(resolve => setTimeout(resolve, 300))
        this.pagination.pageTotal = this.recordList.length
      } catch (error) {
        this.$message.error('Failed to retrieve transaction records')
        console.error('Failed to retrieve transaction records:', error)
      } finally {
        this.loading = false
      }
    },

    handleQuery() {
      this.pagination.pageIndex = 1
      this.fetchRecordList()
    },

    resetQuery() {
      this.queryParams = {
        cardId: '',
        memberId: '',
        spendtype: ''
      }
      this.dateRange = []
      this.handleQuery()
    },

    handleExport() {
      this.$message.info('Export feature under development...')
    },

    async handleView(row) {
      // if no member ID info, try to fetch it
      if (!row.memberId) {
        try {
          const response = await getCardList({
            pageIndex: 1,
            pageSize: 10
          })
          const list = response?.data?.list || []
          if (list.length > 0) {
            const card = list.find(c => c.cardId === row.cardId)
            if (card) {
              row.memberId = card.memberId
            }
          }
        } catch (error) {
          console.error('Failed to retrieve member information:', error)
        }
      }

      this.currentRecord = { ...row }
      this.detailDialogVisible = true
    },

    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.pageIndex = 1
      this.fetchRecordList()
    },

    handleCurrentChange(val) {
      this.pagination.pageIndex = val
      this.fetchRecordList()
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
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

.amount-positive {
  color: #67C23A;
  font-weight: bold;
}

.amount-negative {
  color: #F56C6C;
  font-weight: bold;
}

.integral-positive {
  color: #409EFF;
  font-weight: bold;
}

.integral-negative {
  color: #E6A23C;
  font-weight: bold;
}

.record-detail {
  margin: 20px 0;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>