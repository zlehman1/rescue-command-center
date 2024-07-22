<script setup>
import Footer from "../../Footer.vue";
import Header from "../../Header.vue";
import { useI18n } from "vue-i18n";
import { onMounted, ref } from "vue";
import { useToast } from 'vue-toastification';

const { t } = useI18n();
const users = ref([]);
const editedUser = ref({});
const newUser = ref({
  username: '',
  firstName: '',
  lastName: '',
  password: '',
  repeatedPassword: '',
  isDispatcher: false
});
const isEditDialogOpen = ref(false);
const isAddDialogOpen = ref(false);
const toast = useToast();

const fetchUsers = async () => {
  try {
    const token = localStorage.getItem('jwt');
    const response = await fetch(`/api/v1/users`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    const data = await response.json();
    users.value = data.user;
  } catch (error) {
    console.error('Error fetching users:', error);
  }
};

const editUser = (user) => {
  editedUser.value = {...user};
  isEditDialogOpen.value = true;
};

const saveUser = async () => {
  try {
    const token = localStorage.getItem('jwt');
    const response = await fetch(`/api/v1/users/user?username=${editedUser.value.username}`, {
      method: 'PUT',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        updateType: 'USER',
        firstName: editedUser.value.firstName,
        lastName: editedUser.value.lastName,
        password: ''
      })
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    fetchUsers();
    isEditDialogOpen.value = false;
  } catch (error) {
    console.error('Error saving user:', error);
  }
};

const deleteUser = async (username) => {
  try {
    const token = localStorage.getItem('jwt');
    const response = await fetch(`/api/v1/users/user?username=${username}`, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    fetchUsers();
  } catch (error) {
    console.error('Error deleting user:', error);
  }
};

const addUser = async () => {
  if (newUser.value.password !== newUser.value.repeatedPassword) {
    toast.error('Passwords do not match');
    return;
  }
  try {
    const token = localStorage.getItem('jwt');
    const response = await fetch(`/api/v1/users`, {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        user: {
          username: newUser.value.username,
          firstName: newUser.value.firstName,
          lastName: newUser.value.lastName,
          isDispatcher: Boolean(newUser.value.isDispatcher)
        },
        password: newUser.value.password
      })
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    fetchUsers();
    isAddDialogOpen.value = false;
    newUser.value = {
      username: '',
      firstName: '',
      lastName: '',
      password: '',
      repeatedPassword: '',
      isDispatcher: false
    };
  } catch (error) {
    console.error('Error adding user:', error);
  }
};

onMounted(() => {
  fetchUsers();
});
</script>

<template>
  <v-app>
    <Header :componentName="t('userManagementSettingsTitle')"/>
    <v-main class="main-content">
      <v-container>
        <v-row>
          <v-col
              v-for="user in users"
              :key="user.username"
              cols="12"
              md="6"
          >
            <v-card class="d-flex flex-column fill-height">
              <v-card-title>
                {{ user.username }}
              </v-card-title>
              <v-card-subtitle>
                <p>
                  <v-icon>mdi-account-circle</v-icon>
                  {{ user.firstName }} {{ user.lastName }}
                </p>
              </v-card-subtitle>
              <v-card-text>
                <v-btn class="icons" icon @click="editUser(user)">
                  <v-icon>mdi-pencil</v-icon>
                </v-btn>
                <v-btn class="icons" icon @click="deleteUser(user.username)">
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
      <v-btn icon="" @click="isAddDialogOpen = true" class="add-user-btn">
        <v-icon>mdi-account-plus</v-icon>
      </v-btn>
    </v-main>
    <Footer/>

    <v-dialog v-model="isEditDialogOpen" max-width="500">
      <v-card>
        <v-card-title>
          {{ t('userManagementEditUserTitle') }}
        </v-card-title>
        <v-card-text>
          <v-text-field v-model="editedUser.firstName" :label="t('userManagementFirstnameLabelTitle')"/>
          <v-text-field v-model="editedUser.lastName" :label="t('userManagementLastnameLabelTitle')"/>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn class="dialog-button-cancel" text @click="isEditDialogOpen = false">{{ t('buttonCancel') }}</v-btn>
          <v-btn class="dialog-button-save" text @click="saveUser">{{ t('buttonSave') }}</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="isAddDialogOpen" max-width="500">
      <v-card>
        <v-card-title>
          {{ t('addUserDialogTitle') }}
        </v-card-title>
        <v-card-text>
          <v-text-field v-model="newUser.username" :label="t('userManagementUsernameLabelTitle')"/>
          <v-text-field v-model="newUser.firstName" :label="t('userManagementFirstnameLabelTitle')"/>
          <v-text-field v-model="newUser.lastName" :label="t('userManagementLastnameLabelTitle')"/>
          <v-text-field v-model="newUser.password" :label="t('userManagementPasswordLabelTitle')" type="password"/>
          <v-text-field v-model="newUser.repeatedPassword" :label="t('userManagementPasswordRepeatedLabelTitle')"
                        type="password"/>
          <v-checkbox v-model="newUser.isDispatcher" :label="t('userManagementIsDispatcherLabelTitle')"/>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn class="dialog-button-cancel" text @click="isAddDialogOpen = false">{{ t('buttonCancel') }}</v-btn>
          <v-btn class="dialog-button-save" text @click="addUser">{{ t('buttonSave') }}</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-app>
</template>

<style scoped>
.main-content {
  padding-bottom: 3rem;
  background-color: var(--background-color);
}

.v-card {
  min-height: 100px;
  cursor: pointer;
  background-color: var(--background-greeting-color);
  color: var(--text-greeting-color);
}

.add-user-btn {
  position: fixed;
  bottom: 64px;
  right: 16px;
}

.icons{
  background-color: var(--icon-background-color);
  margin-left: 10px;
}

.dialog-button-save{
  color: var(--dialog-button-save-color);
}

.dialog-button-cancel{
  color: var(--dialog-button-cancel-color);
}
</style>

<style>
:root {
  --background-color-light: white;
  --background-color-dark: #181818;
  --background-color-card-light: white;
  --background-color-card-dark: #8e8e8e;
  --text-color-light: black;
  --text-color-dark: white;
  --icon-background-color-light: white;
  --icon-background-color-dark: #575757;
  --dialog-button-cancel-color-light: red;
  --dialog-button-cancel-color-dark: #a10000;
  --dialog-button-save-color-light: green;
  --dialog-button-save-color-dark: #52fb52;
}

[data-theme="light"] {
  --text-color: var(--text-color-light);
  --background-color: var(--background-color-light);
  --background-color-card: var(--background-color-card-light);
  --icon-background-color: var(--icon-background-color-light);
  --dialog-button-cancel-color: var(--dialog-button-cancel-color-light);
  --dialog-button-save-color: var(--dialog-button-save-color-light);
}

[data-theme="dark"] {
  --text-color: var(--text-color-dark);
  --background-color: var(--background-color-dark);
  --background-color-card: var(--background-color-card-dark);
  --icon-background-color: var(--icon-background-color-dark);
  --dialog-button-cancel-color: var(--dialog-button-cancel-color-dark);
  --dialog-button-save-color: var(--dialog-button-save-color-dark);
}
</style>