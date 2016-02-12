package com.raizlabs.freshair;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Interface which defines the data to show in update prompts. See
 * {@link com.raizlabs.freshair.UpdatePromptInfo.Builder} for easy creation.
 */
public interface UpdatePromptInfo extends Parcelable {
    /**
     * Gets the title text for the regular update case.
     * @param context A {@link Context} to use to access resources.
     * @return The regular title.
     */
    String getTitle(Context context);

    /**
     * Gets the description text for the regular update case.
     * @param context  A {@link Context} to use to access resources.
     * @return The regular description.
     */
    String getDescription(Context context);

    /**
     * Gets the title text for forced updates.
     * @param context A {@link Context} to use to access resources.
     * @return The forced update title.
     */
    String getForcedTitle(Context context);

    /**
     * Gets the description text for forced updates.
     * @param context A {@link Context} to use to access resources.
     * @return The forced update description.
     */
    String getForcedDescription(Context context);

    /**
     * Gets the title text for when the app is disabled from use.
     * @param context A {@link Context} to use to access resources.
     * @return The title for the disabled case.
     */
    String getDisabledTitle(Context context);

    /**
     * Gets the description text for when the app is disabled from use.
     * @param context A {@link Context} to use to access resources.
     * @return The description for the disabled case.
     */
    String getDisabledDescription(Context context);

    /**
     * Gets the label for the button to accept an update.
     * @param context A {@link Context} to use to access resources.
     * @return The label for the button to accept an update.
     */
    String getAcceptString(Context context);

    /**
     * Gets the label for the button to decline an update.
     * @param context A {@link Context} to use to access resources.
     * @return The label for the button to decline an update.
     */
    String getDeclineString(Context context);

    /**
     * Class which helps to easily construct {@link UpdatePromptInfo}s. Starts with all defaults loaded so you can
     * simply override the values you would like changed.
     */
    class Builder implements UpdatePromptInfo {
        private int titleRes;
        private int descriptionRes;

        private int forcedTitleRes;
        private int forcedDescriptionRes;

        private int disabledTitleRes;
        private int disabledDescriptionRes;

        private int acceptRes;
        private int declineRes;

        /**
         * Constructs a new {@link com.raizlabs.freshair.UpdatePromptInfo.Builder} with all defaults loaded.
         */
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

        /**
         * Sets a string resource to be used as the regular update title.
         * @param titleRes The resource ID of the string to use as the title.
         * @return This {@link com.raizlabs.freshair.UpdatePromptInfo.Builder} for chaining method calls.
         */
        public Builder setTitleResource(int titleRes) {
            this.titleRes = titleRes;
            return this;
        }

        @Override
        public String getTitle(Context context) {
            return context.getString(titleRes);
        }

        /**
         * Sets a string resource to be used as the regular update description.
         * @param descriptionRes The resource ID of the string to use as the description.
         * @return This {@link com.raizlabs.freshair.UpdatePromptInfo.Builder} for chaining method calls.
         */
        public Builder setDescriptionResource(int descriptionRes) {
            this.descriptionRes = descriptionRes;
            return this;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(descriptionRes);
        }

        /**
         * Sets a string resource to be used as the forced update title.
         * @param forcedTitleRes The resource ID of the string to use as the title.
         * @return This {@link com.raizlabs.freshair.UpdatePromptInfo.Builder} for chaining method calls.
         */
        public Builder setForcedTitleResource(int forcedTitleRes) {
            this.forcedTitleRes = forcedTitleRes;
            return this;
        }

        @Override
        public String getForcedTitle(Context context) {
            return context.getString(forcedTitleRes);
        }

        /**
         * Sets a string resource to be used as the forced update description.
         * @param forcedDescriptionRes The resource ID of the string to use as the description.
         * @return This {@link com.raizlabs.freshair.UpdatePromptInfo.Builder} for chaining method calls.
         */
        public Builder setForcedDescriptionResource(int forcedDescriptionRes) {
            this.forcedDescriptionRes = forcedDescriptionRes;
            return this;
        }

        @Override
        public String getDisabledTitle(Context context) {
            return context.getString(disabledTitleRes);
        }

        /**
         * Sets a string resource to be used as the title when the app is disabled from use.
         * @param disabledTitleRes The resource ID of the string ot use as the title.
         * @return This {@link com.raizlabs.freshair.UpdatePromptInfo.Builder} for chaining method calls.
         */
        public Builder setDisabledTitleResource(int disabledTitleRes) {
            this.disabledTitleRes = disabledTitleRes;
            return this;
        }

        @Override
        public String getDisabledDescription(Context context) {
            return context.getString(disabledDescriptionRes);
        }

        /**
         * Sets a string resource to be used as the description when the app is disabled from use.
         * @param disabledDescriptionRes The resource ID of the string to use as the description.
         * @return This {@link com.raizlabs.freshair.UpdatePromptInfo.Builder} for chaining method calls.
         */
        public Builder setDisabledDescriptionResource(int disabledDescriptionRes) {
            this.disabledDescriptionRes = disabledDescriptionRes;
            return this;
        }

        @Override
        public String getForcedDescription(Context context) {
            return context.getString(forcedDescriptionRes);
        }

        /**
         * Sets a string resource to be used as the label for the button to accept an update.
         * @param acceptRes The resource ID of the string to use as the label.
         * @return This {@link com.raizlabs.freshair.UpdatePromptInfo.Builder} for chaining method calls.
         */
        public Builder setAcceptStringResource(int acceptRes) {
            this.acceptRes = acceptRes;
            return this;
        }

        @Override
        public String getAcceptString(Context context) {
            return context.getString(acceptRes);
        }

        /**
         * Sets a string resource to be used as the label for the button to decline an update.
         * @param declineRes The resource ID of the string to use as the label.
         * @return This {@link com.raizlabs.freshair.UpdatePromptInfo.Builder} for chaining method calls.
         */
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
