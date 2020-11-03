// eslint-disable-next-line
import { UserLayout, BasicLayout, BlankLayout } from '@/layouts'
import { bxAnaalyse } from '@/core/icons'

const RouteView = {
  name: 'RouteView',
  render: (h) => h('router-view')
}

export const asyncRouterMap = [

  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: 'menu.home' },
    redirect: '/dashboard/workplace',
    children: [
      {
        path: '/operation',
        name: 'operation',
        component: () => import('@/views/operation'),
        meta: { title: '数字运营', keepAlive: true, icon: bxAnaalyse }
      },
      {
        path: '/interaction',
        name: 'interaction',
        component: RouteView,
        redirect: '/interaction/qrcode',
        meta: { title: '互动推送', keepAlive: true, icon: 'code' },
        children: [
          {
            path: '/interaction/qrcode',
            name: 'interactionQrcode',
            component: () => import('@/views/interaction/qrcode/qrcode'),
            meta: { title: '渠道二维码', keepAlive: false }
          },
          {
            path: '/interaction/createQrcode',
            name: 'interactionCreateQrcode',
            hidden: true,
            component: () => import('@/views/interaction/qrcode/createQrcode'),
            meta: { title: '编辑二维码', keepAlive: false, hidden: false }
          },
          {
            path: '/interaction/followReply',
            name: 'interactionFollowReply',
            component: () => import('@/views/interaction/followReply'),
            meta: { title: '被关注回复', keepAlive: false, hidden: true }
          },
          {
            path: '/interaction/editReply',
            name: 'interactionEditReply',
            hidden: true,
            component: () => import('@/views/interaction/followReply/EditReply'),
            meta: { title: '编辑被关注回复', keepAlive: false, hidden: true }
          },
          {
            path: '/interaction/push',
            name: 'interactionPush',
            component: () => import('@/views/interaction/push'),
            meta: { title: '智能推送', keepAlive: false, hidden: true }
          },
          {
            path: '/interaction/editPush',
            name: 'interactionEditPush',
            hidden: true,
            component: () => import('@/views/interaction/push/EditPush'),
            meta: { title: '编辑智能推送', keepAlive: false, hidden: true }
          }
        ]
      },
      {
        path: '/fans',
        name: 'fans',
        component: RouteView,
        meta: { title: '粉丝管理', keepAlive: true, icon: 'user' },
        children: [
          {
            path: '/fans/list',
            name: 'fansList',
            component: () => import('@/views/fans/list'),
            meta: { title: '粉丝列表', keepAlive: false, hidden: true }
          },
          {
            path: '/fans/tag',
            name: 'fansTag',
            component: () => import('@/views/fans/tag'),
            meta: { title: '标签管理', keepAlive: false, hidden: true }
          }
        ]
      },
      {
        path: '/information',
        name: 'information',
        component: RouteView,
        meta: { title: '智能群发', keepAlive: true, icon: 'sound' },
        children: [
          {
            path: '/information/customer',
            name: 'customer',
            component: () => import('@/views/information/customer'),
            meta: { title: '客服消息', keepAlive: false }
          },
          {
            path: '/information/customer/add',
            name: 'customerAdd',
            hidden: true,
            component: () => import('@/views/information/customer/add'),
            meta: { title: '编辑客服消息', keepAlive: false }
          },
          {
            path: '/information/mass',
            name: 'mass',
            component: () => import('@/views/information/mass'),
            meta: { title: '高级群发', keepAlive: false }
          },
          {
            path: '/information/mass/add',
            name: 'massAdd',
              hidden: true,
            component: () => import('@/views/information/mass/add'),
            meta: { title: '编辑高级群发', keepAlive: false }
          },
          {
            path: '/information/template',
            name: 'template',
            component: () => import('@/views/information/template'),
            meta: { title: '模板消息', keepAlive: false }
          },
          {
            path: '/information/template/add',
            name: 'templateAdd',
            hidden: true,
            component: () => import('@/views/information/template/add'),
            meta: { title: '编辑模板消息', keepAlive: false }
          }
        ]
      },
      {
        path: '/publicAccAdmin',
        name: 'publicAccAdmin',
        component: () => import('@/views/publicAccount'),
        meta: { title: '公众号管理', keepAlive: true, icon: 'qrcode' }
      },
      {
        path: '/confirm',
        name: 'confirm',
        hidden: true,
        component: () => import('@/views/publicAccount/confirm'),
        meta: { title: '确认', keepAlive: true, icon: 'qrcode' }
      },
      {
        path: '/user/admin',
        name: 'userAdmin',
        component: () => import('@/views/account/admin'),
        meta: { title: '用户管理', keepAlive: true, icon: 'user' }
      }
      // {
      //   path: '/invite',
      //   name: 'invite',
      //   component: () => import('@/views/invite/list'),
      //   meta: { title: '代理商', keepAlive: true, icon: bxAnaalyse }
      // }
      // forms
      // {
      //   path: '/form',
      //   redirect: '/form/base-form',
      //   component: RouteView,
      //   meta: { title: '表单页', icon: 'form', permission: [ 'form' ] },
      //   children: [
      //     {
      //       path: '/form/base-form',
      //       name: 'BaseForm',
      //       component: () => import('@/views/form/basicForm'),
      //       meta: { title: '基础表单', keepAlive: true, permission: [ 'form' ] }
      //     },
      //     {
      //       path: '/form/step-form',
      //       name: 'StepForm',
      //       component: () => import('@/views/form/stepForm/StepForm'),
      //       meta: { title: '分步表单', keepAlive: true, permission: [ 'form' ] }
      //     },
      //     {
      //       path: '/form/advanced-form',
      //       name: 'AdvanceForm',
      //       component: () => import('@/views/form/advancedForm/AdvancedForm'),
      //       meta: { title: '高级表单', keepAlive: true, permission: [ 'form' ] }
      //     }
      //   ]
      // }
      // Exception
      // {
      //   path: '/exception',
      //   name: 'exception',
      //   component: RouteView,
      //   redirect: '/exception/403',
      //   meta: { title: '异常页', icon: 'warning', permission: [ 'exception' ] },
      //   children: [
      //     {
      //       path: '/exception/403',
      //       name: 'Exception403',
      //       component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/403'),
      //       meta: { title: '403', permission: [ 'exception' ] }
      //     },
      //     {
      //       path: '/exception/404',
      //       name: 'Exception404',
      //       component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404'),
      //       meta: { title: '404', permission: [ 'exception' ] }
      //     },
      //     {
      //       path: '/exception/500',
      //       name: 'Exception500',
      //       component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/500'),
      //       meta: { title: '500', permission: [ 'exception' ] }
      //     }
      //   ]
      // },

      // account
      // {
      //   path: '/account',
      //   component: RouteView,
      //   redirect: '/account/center',
      //   name: 'account',
      //   meta: { title: '个人页', icon: 'user', keepAlive: true, permission: [ 'user' ] },
      //   children: [
      //     {
      //       path: '/account/center',
      //       name: 'center',
      //       component: () => import('@/views/account/center'),
      //       meta: { title: '个人中心', keepAlive: true, permission: [ 'user' ] }
      //     },
      //     {
      //       path: '/account/settings',
      //       name: 'settings',
      //       component: () => import('@/views/account/settings/Index'),
      //       meta: { title: '个人设置', hideHeader: true, permission: [ 'user' ] },
      //       redirect: '/account/settings/base',
      //       hideChildrenInMenu: true,
      //       children: [
      //         {
      //           path: '/account/settings/base',
      //           name: 'BaseSettings',
      //           component: () => import('@/views/account/settings/BaseSetting'),
      //           meta: { title: '基本设置', hidden: true, permission: [ 'user' ] }
      //         },
      //         {
      //           path: '/account/settings/security',
      //           name: 'SecuritySettings',
      //           component: () => import('@/views/account/settings/Security'),
      //           meta: { title: '安全设置', hidden: true, keepAlive: true, permission: [ 'user' ] }
      //         },
      //         {
      //           path: '/account/settings/custom',
      //           name: 'CustomSettings',
      //           component: () => import('@/views/account/settings/Custom'),
      //           meta: { title: '个性化设置', hidden: true, keepAlive: true, permission: [ 'user' ] }
      //         },
      //         {
      //           path: '/account/settings/binding',
      //           name: 'BindingSettings',
      //           component: () => import('@/views/account/settings/Binding'),
      //           meta: { title: '账户绑定', hidden: true, keepAlive: true, permission: [ 'user' ] }
      //         },
      //         {
      //           path: '/account/settings/notification',
      //           name: 'NotificationSettings',
      //           component: () => import('@/views/account/settings/Notification'),
      //           meta: { title: '新消息通知', hidden: true, keepAlive: true, permission: [ 'user' ] }
      //         }
      //       ]
      //     }
      //   ]
      // }

      // other
      /*
      {
        path: '/other',
        name: 'otherPage',
        component: PageView,
        meta: { title: '其他组件', icon: 'slack', permission: [ 'dashboard' ] },
        redirect: '/other/icon-selector',
        children: [
          {
            path: '/other/icon-selector',
            name: 'TestIconSelect',
            component: () => import('@/views/other/IconSelectorView'),
            meta: { title: 'IconSelector', icon: 'tool', keepAlive: true, permission: [ 'dashboard' ] }
          },
          {
            path: '/other/list',
            component: RouteView,
            meta: { title: '业务布局', icon: 'layout', permission: [ 'support' ] },
            redirect: '/other/list/tree-list',
            children: [
              {
                path: '/other/list/tree-list',
                name: 'TreeList',
                component: () => import('@/views/other/TreeList'),
                meta: { title: '树目录表格', keepAlive: true }
              },
              {
                path: '/other/list/edit-table',
                name: 'EditList',
                component: () => import('@/views/other/TableInnerEditList'),
                meta: { title: '内联编辑表格', keepAlive: true }
              },
              {
                path: '/other/list/user-list',
                name: 'UserList',
                component: () => import('@/views/other/UserList'),
                meta: { title: '用户列表', keepAlive: true }
              },
              {
                path: '/other/list/role-list',
                name: 'RoleList',
                component: () => import('@/views/other/RoleList'),
                meta: { title: '角色列表', keepAlive: true }
              },
              {
                path: '/other/list/system-role',
                name: 'SystemRole',
                component: () => import('@/views/role/RoleList'),
                meta: { title: '角色列表2', keepAlive: true }
              },
              {
                path: '/other/list/permission-list',
                name: 'PermissionList',
                component: () => import('@/views/other/PermissionList'),
                meta: { title: '权限列表', keepAlive: true }
              }
            ]
          }
        ]
      }
      */
    ]
  },
  {
    path: '*', redirect: '/404', hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/',
    redirect: '/user'
  },
  {
    path: '/user',
    component: UserLayout,
    redirect: '/login',
    hidden: true,
    children: [
      {
        path: '/login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      },
      {
        path: '/register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Register')
      },
      {
        path: '/register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/RegisterResult')
      },
      {
        path: '/recover',
        name: 'recover',
        component: undefined
      }
    ]
  },

  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }

]
