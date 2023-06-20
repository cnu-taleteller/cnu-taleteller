<template>
  <div class="book-reply">
    <form @submit.prevent="submitReply" class="reply-form">
      <input type="text" v-model="replyContent" placeholder="댓글을 입력하세요">
      <button type="submit">등록</button>
    </form>
    <ul class="reply-list">
      <li v-for="reply in replies" :key="reply.replyId" class="reply-item">
        <div class="reply-author">{{ reply.member.memberName }}</div>
        <div class="reply-content">
          <div v-if="editMode === reply.replyId">
          <input type="text" v-model="newReplyContent">
          </div>
          <div v-else>{{ reply.replyContent }}</div>
        </div>
        <div class="reply-time">{{ reply.replyRegdate }}</div>
        <div v-if="reply.member.memberEmail === sessionStorage.getItem('user')" class="reply-actions">
          <button @click="editReply(reply)" class="edit-button">{{ editMode === reply.replyId ? '저장' : '수정' }}</button>
          <button @click="deleteReply(reply)" class="delete-button">삭제</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    replies: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      replyContent: "",
      sessionStorage: sessionStorage,
      editMode: null,
      newReplyContent: ""
    }
  },
  methods: {
    submitReply() {
      this.$emit("addReply", this.replyContent);
      this.replyContent = "";
    },
    editReply(reply) {
      if (this.editMode === reply.replyId) {
        const replyContent = this.newReplyContent;
        if (replyContent) {
          this.$emit('editReply', reply.replyId, replyContent);
          this.newReplyContent = '';
          this.editMode = null;
        }
      } else {
        this.newReplyContent = reply.replyContent;
        this.editMode = reply.replyId;
      }
    },
    deleteReply(reply) {
      alert("삭제하시겠습니까?");
      this.$emit("deleteReply", reply.replyId);
    },
  }
}
</script>

<style scoped>
.book-reply form {
  display: flex;
  margin-left: 5%;
  margin-top: 5%;
}

.reply-form input[type="text"] {
  flex-basis: 90%;
  padding: 10px;
  font-size: 16px;
  color: #333;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.book-reply button[type="submit"] {
  flex-basis: 10%;
  padding: 10px 20px;
  font-size: 16px;
  color: #fff;
  border: none;
  border-radius: 5px;
  background-color: #faab19;
  cursor: pointer;
  margin-left: 10px;
}

.book-reply button[type="submit"]:hover {
  background-color: #e79e16;
}

.book-reply button[type="submit"]:active {
  background-color: #e79e16;
}

.reply-list {
  list-style-type: none;
  padding-left: 0;
}

.reply-item {
  display: flex;
  flex-wrap: wrap;
  margin-top: 10px;
}

.reply-author {
  font-weight: bold;
  flex-basis: 20%;
  align-self: flex-start;
}

.reply-content, edit-form {
  flex-basis: 50%;
  text-align: left;
}

.reply-time {
  flex-basis: 20%;
  text-align: right;
}

.reply-item .reply-actions {
  flex-basis: 10%;
  align-self: flex-end;
}

.reply-item button {
  margin-left: 5px;
  padding: 5px 5px;
  font-size: 14px;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.reply-item .edit-button {
  background-color: #6db6ff;
}

.reply-item .edit-button:hover {
  background-color: #499bea;
}

.reply-item .delete-button {
  background-color: #ff7070;
}

.reply-item .delete-button:hover {
  background-color: #e64848;
}

.reply-content input[type="text"] {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  color: #333;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>
