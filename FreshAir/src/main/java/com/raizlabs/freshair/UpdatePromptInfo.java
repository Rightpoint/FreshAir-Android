package com.raizlabs.freshair;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public interface UpdatePromptInfo extends Parcelable {
    String getTitle(Context context);
    String getDescription(Context context);

    String getForcedTitle(Context context);
    String getForcedDescription(Context context);

    String getDisabledTitle(Context context);
    String getDisabledDescription(Context context);

    String getAcceptString(Context context);
    String getDeclineString(Context context);

    class Builder implements UpdatePromptInfo {
        private int titleRes;
        private int descriptionRes;

        private int forcedTitleRes;
        private int forcedDescriptionRes;

        private int disabledTitleRes;
        private int disabledDescriptionRes;

        private int acceptRes;
        private int declineRes;

        public Builder() {
            setTitleResource(R.string.UpdatePrompt_Title_Default);
            setDescriptionResource(R.string.UpdatePrompt_Description_Default);

            setForcedTitleResource(R.string.UpdatePrompt_ForcedTitle_Default);
            setForcedDescriptionResource(R.string.UpdatePrompt_ForcedDescription_Default);

            setDisabledTitleResource(R.string.UpdatePrompt_DisabledTitle_Default);
            setDisabledDescriptionResource(R.string.UpdatePrompt_DisabledDescription_Default);

            setAcceptStringResource(R.string.UpdatePrompt_Accept_Default);
            setDeclineStringResource(R.string.UpdatePrompt_Decline_Default);
        }

        public Builder setTitleResource(int titleRes) {
            this.titleRes = titleRes;
            return this;
        }

        @Override
        public String getTitle(Context context) {
            return context.getString(titleRes);
        }

        public Builder setDescriptionResource(int descriptionRes) {
            this.descriptionRes = descriptionRes;
            return this;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(descriptionRes);
        }

        public Builder setForcedTitleResource(int forcedTitleRes) {
            this.forcedTitleRes = forcedTitleRes;
            return this;
        }

        @Override
        public String getForcedTitle(Context context) {
            return context.getString(forcedTitleRes);
        }

        public Builder setForcedDescriptionResource(int forcedDescriptionRes) {
            this.forcedDescriptionRes = forcedDescriptionRes;
            return this;
        }

        @Override
        public String getDisabledTitle(Context context) {
            return context.getString(disabledTitleRes);
        }

        public Builder setDisabledTitleResource(int disabledTitleRes) {
            this.disabledTitleRes = disabledTitleRes;
            return this;
        }

        @Override
        public String getDisabledDescription(Context context) {
            return context.getString(disabledDescriptionRes);
        }

        public Builder setDisabledDescriptionResource(int disabledDescriptionRes) {
            this.disabledDescriptionRes = disabledDescriptionRes;
            return this;
        }

        @Override
        public String getForcedDescription(Context context) {
            return context.getString(forcedDescriptionRes);
        }

        public Builder setAcceptStringResource(int acceptRes) {
            this.acceptRes = acceptRes;
            return this;
        }

        @Override
        public String getAcceptString(Context context) {
            return context.getString(acceptRes);
        }

        public Builder setDeclineStringResource(int declineRes) {
            this.declineRes = declineRes;
            return this;
        }

        @Override
        public String getDeclineString(Context context) {
            return context.getString(declineRes);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(titleRes);
            dest.writeInt(descriptionRes);

            dest.writeInt(forcedTitleRes);
            dest.writeInt(forcedDescriptionRes);

            dest.writeInt(disabledTitleRes);
            dest.writeInt(disabledDescriptionRes);

            dest.writeInt(acceptRes);
            dest.writeInt(declineRes);

        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel source) {
                Builder builder = new Builder();
                builder.setTitleResource(source.readInt());
                builder.setDescriptionResource(source.readInt());

                builder.setForcedTitleResource(source.readInt());
                builder.setForcedDescriptionResource(source.readInt());

                builder.setDisabledTitleResource(source.readInt());
                builder.setDisabledDescriptionResource(source.readInt());

                builder.setAcceptStringResource(source.readInt());
                builder.setDeclineStringResource(source.readInt());


                return builder;
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[0];
            }
        };
    }
}
