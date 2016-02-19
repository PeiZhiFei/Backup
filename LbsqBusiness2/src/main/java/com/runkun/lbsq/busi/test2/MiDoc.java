package com.runkun.lbsq.busi.test2; //检查更新，可以动态添加界面组件
 //   public void update()
  //  {
//        XiaomiUpdateAgent.setUpdateAutoPopup(false);
//        XiaomiUpdateAgent.setUpdateListener(new XiaomiUpdateListener() {
//            @Override
//            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//                switch (updateStatus) {
//                    case UpdateStatus.STATUS_UPDATE:
//                        showUpdate(updateInfo.path);
//                        // 有更新， UpdateResponse为本次更新的详细信息
//                        break;
//                    case UpdateStatus.STATUS_NO_UPDATE:
//                        // 无更新， UpdateResponse为null
//                        break;
//                    case UpdateStatus.STATUS_NO_WIFI:
//                        // 设置了只在WiFi下更新，且WiFi不可用时， UpdateResponse为null
//                        break;
//                    case UpdateStatus.STATUS_NO_NET:
//                        // 没有网络， UpdateResponse为null
//                        break;
//                    case UpdateStatus.STATUS_FAILED:
//                        // 检查更新与服务器通讯失败，可稍后再试， UpdateResponse为null
//                        break;
//                    case UpdateStatus.STATUS_LOCAL_APP_FAILED:
//                        // 检查更新获取本地安装应用信息失败， UpdateResponse为null
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//        XiaomiUpdateAgent.update(this);
//    }

    //使用MaterialDesignDialog显示进度
 //   private void showUpdate(final String path)
//    {
//        new MaterialDialog.Builder(MainActivity.this)
//                .title("软件更新")
//                .titleGravity(GravityEnum.CENTER)
//                .titleColorRes(android.R.color.holo_purple)
//                .content("发现新版本了，快来下载吧！")
//                .contentGravity(GravityEnum.CENTER)
//                .contentColorRes(android.R.color.black)
//                .theme(Theme.LIGHT)
//                .positiveText("确定")
//                .positiveColorRes(android.R.color.holo_blue_dark)
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onPositive(MaterialDialog dialog) {
//                        new Download().download(MainActivity.this, path);
//                    }
//                })
//                .show();
        //或者使用另一个对话框不是通知栏
//        new Download ().download (NotifyActivityAdd.this, url);


    //需要引用recycleview

        //    public static void dialog(Context context) {
        //        new MaterialDialog.Builder(context).show();
        //        //使用预置的adapter和自定义adapter
        //        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(context);
        //        adapter.add(new MaterialSimpleListItem.Builder(context)
        //                .content("username@gmail.com")
        //                .icon(R.drawable.ic_check)
        //                .build());
        //        adapter.add(new MaterialSimpleListItem.Builder(context)
        //                .content("user02@gmail.com")
        //                .icon(R.drawable.ic_check)
        //                .build());
        //        adapter.add(new MaterialSimpleListItem.Builder(context)
        //                .content("content")
        //                .icon(R.drawable.ic_add)
        //                .build());
        //
        //        new MaterialDialog.Builder(context)
        //                //设置图标
        //                .iconRes(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
        //                .limitIconToDefaultSize() // limits the displayed icon size to 48dp
        //                        //可以无标题
        //                .title("title")
        //                .titleGravity(GravityEnum.CENTER)//居中
        //                .titleColorRes(R.color.white)
        //
        //                .content("content")
        //                .contentGravity(GravityEnum.CENTER)
        //                .contentLineSpacing(1.6f)//内容间距
        //                .positiveText("确定")
        //                .negativeText("取消")
        //                .neutralText("中性")
        //                        //样式
        //                .positiveColorRes(R.color.material_blue_grey_800)
        //                .negativeColorRes(R.color.material_blue_grey_800)
        //                .contentColorRes(android.R.color.white)
        //                        //背景色
        //                .backgroundColorRes(R.color.material_blue_grey_800)
        //                .dividerColorRes(R.color.material_blue_grey_800)
        //                        //按钮选择器
        //                .btnSelector(R.drawable.md_btn_selector, DialogAction.POSITIVE)
        //                .positiveColor(Color.WHITE)
        //                .negativeColorAttr(android.R.attr.textColorSecondaryInverse)
        //                        //主题
        //                .theme(Theme.DARK)
        //
        //                        //设置按钮竖排
        ////                .btnStackedGravity(GravityEnum.END)
        ////                .forceStacking(true)  // this generally should not be forced, but is used for demo purposes
        //                        //监听
        //                .callback(new MaterialDialog.ButtonCallback() {
        //                    @Override
        //                    public void onPositive(MaterialDialog dialog) {
        //                    }
        //
        //                    @Override
        //                    public void onNeutral(MaterialDialog dialog) {
        //                    }
        //
        //                    @Override
        //                    public void onNegative(MaterialDialog dialog) {
        //                    }
        //                })
        //                        //列表
        ////                .items(R.array.socialNetworks)
        //                .itemsCallback(new MaterialDialog.ListCallback() {
        //                    @Override
        //                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
        //                    }
        //                })
        //                        //单选回调
        //                .itemsCallbackSingleChoice(2, new MaterialDialog.ListCallbackSingleChoice() {
        //                    @Override
        //                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
        //                        return true; // allow selection
        //                    }
        //                })
        //                        //多选回调
        //                .itemsCallbackMultiChoice(new Integer[]{1, 3}, new MaterialDialog.ListCallbackMultiChoice() {
        //                    @Override
        //                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
        //                        StringBuilder str = new StringBuilder();
        //                        for (int i = 0; i < which.length; i++) {
        //                            if (i > 0) str.append('\n');
        //                            str.append(which[i]);
        //                            str.append(": ");
        //                            str.append(text[i]);
        //                        }
        //                        return true; // allow selection
        //                    }
        //                    //有限制多选个数的写法
        ////                    @Override
        ////                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
        ////                        boolean allowSelection = which.length <= 2; // limit selection to 2, the new selection is included in the which array
        ////                        if (!allowSelection) {
        ////                            s(R.string.selection_limit_reached);
        ////                        }
        ////                        return allowSelection;
        ////                    }
        //                })
        //
        ////                .alwaysCallMultiChoiceCallback()
        //                        //使用adapter
        //                .adapter(adapter, new MaterialDialog.ListCallback() {
        //                    @Override
        //                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        //                        MaterialSimpleListItem item = adapter.getItem(which);
        ////                        s(item.getContent().toString());
        //                    }
        //                })
        //                        //文本框
        ////                .inputType(InputType.TYPE_CLASS_TEXT |
        ////                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
        ////                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
        ////                .inputMaxLength(16)
        ////                .input("提示", "我是提示", false, new MaterialDialog.InputCallback() {
        ////                    @Override
        ////                    public void onInput(MaterialDialog dialog, CharSequence input) {
        ////                    }
        ////                })
        //                        //圆圈进度
        ////                .progress(true, 0)
        //                        //水平进度
        ////                .progress(false, 150, true)
        ////                .showListener(new DialogInterface.OnShowListener() {
        ////                    @Override
        ////                    public void onShow(DialogInterface dialogInterface) {
        ////                        final MaterialDialog dialog = (MaterialDialog) dialogInterface;
        ////                        while (dialog.getCurrentProgress() != dialog.getMaxProgress() &&
        ////                                !Thread.currentThread().isInterrupted()) {
        ////                            if (dialog.isCancelled())
        ////                                break;
        ////                            try {
        ////                                Thread.sleep(50);
        ////                            } catch (InterruptedException e) {
        ////                                break;
        ////                            }
        ////                            dialog.incrementProgress(1);
        ////                        }
        ////                    }
        ////                })
        ////                .cancelListener(new DialogInterface.OnCancelListener() {
        ////                    @Override
        ////                    public void onCancel(DialogInterface dialog) {
        ////                    }
        ////                })
        ////                .dismissListener(new DialogInterface.OnDismissListener() {
        ////                    @Override
        ////                    public void onDismiss(DialogInterface dialog) {
        ////                    }
        ////                })
        //                .show();
        //
        //
        //
        //    }
        //
        ////    public static void dialogWeb(Context context) {
        ////        int accentColor = ThemeSingleton.get().widgetColor;
        ////        if (accentColor == 0)
        ////            accentColor = context.getResources().getColor(R.color.material_blue_grey_800);
        ////        MaterialWebDialog.create(false, accentColor)
        ////                .show(((FragmentActivity) context).getSupportFragmentManager(), "changelog");
        ////    }
        //
        ////    public static void dialogFile(Context context) {
        ////        new FolderSelectorDialog().show((AppCompatActivity) context);
        //    }
        //
        //    //颜色选择器
        ////        new ColorChooserDialog().show(this, selectedColorIndex);
        //

