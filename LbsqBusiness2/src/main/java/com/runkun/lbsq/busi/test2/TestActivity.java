package com.runkun.lbsq.busi.test2; /**
        //占位图没成功
        //        Uri uri = Uri.parse ("http://pic26.nipic.com/20121219/11585796_091133960000_2.jpg");
        //        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById (R.id.my_image_view);
        //        draweeView.setImageURI (uri);
        //        GenericDraweeHierarchyBuilder builder =
        //                new GenericDraweeHierarchyBuilder (getResources ());
        //        GenericDraweeHierarchy hierarchy = builder
        //                .setFadeDuration (300)
        //                .setProgressBarImage (new ProgressBarDrawable ())
        //                .build ();
        //        draweeView.setHierarchy (hierarchy);

//
//        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {
//            @Override
//            public int getNextScanNumberToDecode(int scanNumber) {
//                return scanNumber + 2;
//            }
//
//            public QualityInfo getQualityInfo(int scanNumber) {
//                boolean isGoodEnough = (scanNumber >= 5);
//                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
//            }
//        };

//                        ImagePipelineConfig config = ImagePipelineConfig.newBuilder()
//                                .setProgressiveJpegConfig (pjpeg)
//                                .build ();
//
//                        ImageRequest request = ImageRequestBuilder
//                                .newBuilderWithSource(uri)
//                                .setProgressiveRenderingEnabled(true)
//                                .build();
//                        PipelineDraweeController controller = Fresco.newControllerBuilder()
//                                .setImageRequest (request)
//                                .setOldController (draweeView.getController ())
//                                .build ();
//
//                        draweeView.setController(controller);

//        SimpleDraweeView aniView = (SimpleDraweeView) findViewById(R.id.my_image_view);
//        Uri aniImageUri = Uri.parse("https://camo.githubusercontent.com/588a2ef2cdcfb6c71e88437df486226dd15605b3/687474703a2f2f737261696e2d6769746875622e71696e6975646e2e636f6d2f756c7472612d7074722f73746f72652d686f7573652d737472696e672d61727261792e676966");
//        //        Uri aniImageUri = Uri.parse("http://f2.market.xiaomi.com/download/ThemeMarket/0588ff4cc01e44ec6355aced54eb1bb7ac4ea3606/Road+Scape-1.0.0.2.jpg");
//        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(aniImageUri)
//                .build();
//
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setImageRequest(request)
//                .setAutoPlayAnimations(true)
//                .build();
//        aniView.setController(controller);



    public static void dialog(Context context) {
        new MaterialDialog.Builder(context).show();
        //使用预置的adapter和自定义adapter
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(context);
        adapter.add(new MaterialSimpleListItem.Builder(context)
                .content("username@gmail.com")
                .icon(R.drawable.origianlprice)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(context)
                .content("user02@gmail.com")
                .icon(R.drawable.origianlprice)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(context)
                .content("content")
                .icon(R.drawable.origianlprice)
                .build());

        new MaterialDialog.Builder(context)
                //设置图标
                .iconRes(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
                .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                        //可以无标题
                .title("title")
                .titleGravity(GravityEnum.CENTER)//居中
                .titleColorRes(R.color.white)

                .content("content")
                .contentGravity(GravityEnum.CENTER)
                .contentLineSpacing(1.6f)//内容间距
                .positiveText("确定")
                .negativeText("取消")
                .neutralText("中性")
                        //样式
                .positiveColorRes(R.color.material_blue_grey_800)
                .negativeColorRes(R.color.material_blue_grey_800)
                .contentColorRes(android.R.color.white)
                        //背景色
                .backgroundColorRes(R.color.material_blue_grey_800)
                .dividerColorRes(R.color.material_blue_grey_800)
                        //按钮选择器
                .btnSelector(R.drawable.md_btn_selector, DialogAction.POSITIVE)
                .positiveColor(Color.WHITE)
                .negativeColorAttr(android.R.attr.textColorSecondaryInverse)
                        //主题
                .theme(Theme.DARK)

                        //设置按钮竖排
                        //                .btnStackedGravity(GravityEnum.END)
                        //                .forceStacking(true)  // this generally should not be forced, but is used for demo purposes
                        //监听
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                })
                        //列表
                        //                .items(R.array.socialNetworks)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                    }
                })
                        //单选回调
                .itemsCallbackSingleChoice(2, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        return true; // allow selection
                    }
                })
                        //多选回调
                .itemsCallbackMultiChoice(new Integer[]{1, 3}, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        StringBuilder str = new StringBuilder();
                        for (int i = 0; i < which.length; i++) {
                            if (i > 0) str.append('\n');
                            str.append(which[i]);
                            str.append(": ");
                            str.append(text[i]);
                        }
                        return true; // allow selection
                    }
                    //有限制多选个数的写法
                    //                    @Override
                    //                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                    //                        boolean allowSelection = which.length <= 2; // limit selection to 2, the new selection is included in the which array
                    //                        if (!allowSelection) {
                    //                            s(R.string.selection_limit_reached);
                    //                        }
                    //                        return allowSelection;
                    //                    }
                })

                        //                .alwaysCallMultiChoiceCallback()
                        //使用adapter
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        MaterialSimpleListItem item = adapter.getItem(which);
                        //                        s(item.getContent().toString());
                    }
                })
                        //文本框
                        //                .inputType(InputType.TYPE_CLASS_TEXT |
                        //                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        //                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        //                .inputMaxLength(16)
                        //                .input("提示", "我是提示", false, new MaterialDialog.InputCallback() {
                        //                    @Override
                        //                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        //                    }
                        //                })
                        //圆圈进度
                        //                .progress(true, 0)
                        //水平进度
                        //                .progress(false, 150, true)
                        //                .showListener(new DialogInterface.OnShowListener() {
                        //                    @Override
                        //                    public void onShow(DialogInterface dialogInterface) {
                        //                        final MaterialDialog dialog = (MaterialDialog) dialogInterface;
                        //                        while (dialog.getCurrentProgress() != dialog.getMaxProgress() &&
                        //                                !Thread.currentThread().isInterrupted()) {
                        //                            if (dialog.isCancelled())
                        //                                break;
                        //                            try {
                        //                                Thread.sleep(50);
                        //                            } catch (InterruptedException e) {
                        //                                break;
                        //                            }
                        //                            dialog.incrementProgress(1);
                        //                        }
                        //                    }
                        //                })
                        //                .cancelListener(new DialogInterface.OnCancelListener() {
                        //                    @Override
                        //                    public void onCancel(DialogInterface dialog) {
                        //                    }
                        //                })
                        //                .dismissListener(new DialogInterface.OnDismissListener() {
                        //                    @Override
                        //                    public void onDismiss(DialogInterface dialog) {
                        //                    }
                        //                })
                .show();


*/
