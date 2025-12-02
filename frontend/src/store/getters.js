const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  roles: state => state.user.roles,
  memberList: state => state.member.memberList,
  cardList: state => state.member.cardList,
  totalMembers: state => state.member.totalMembers,
  totalCards: state => state.member.totalCards
}
export default getters